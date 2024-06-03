/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import java.util.*;
import java.util.function.*;
import java.security.*;

public interface Digest {
    public static final Algorithm SHA256 = Builtin.alg("SHA-256", 32, 64);
    public static final Algorithm SHA384 = Builtin.alg("SHA-384", 48, 128);
    public static final Algorithm SHA512 = Builtin.alg("SHA-512", 64, 128);
    public static final Algorithm SHA1 = Builtin.alg("SHA-1", 20, 64);
    public static final Algorithm MD5 = Builtin.alg("MD5", 16, 64);

    public static interface Algorithm extends Supplier<Digest> {
	public int diglen();
	public int blocklen();
    }

    public Digest update(byte[] buf, int off, int len);
    public default Digest update(byte[] part) {return(update(part, 0, part.length));}
    public byte[] digest();
    public Digest copy();
    public default String hexdigest() {
	return(Utils.byte2hex(digest()));
    }

    public static byte[] hash(Algorithm alg, byte[]... parts) {
	Digest dig = alg.get();
	for(byte[] part : parts)
	    dig.update(part);
	return(dig.digest());
    }

    public static class Builtin implements Digest {
	private final MessageDigest md;

	Builtin(MessageDigest md) {
	    this.md = md;
	}

	public Digest update(byte[] buf, int off, int len) {
	    md.update(buf, off, len);
	    return(this);
	}

	public byte[] digest() {
	    return(md.digest());
	}

	public Digest copy() {
	    try {
		return(new Builtin((MessageDigest)md.clone()));
	    } catch(CloneNotSupportedException e) {
		throw(new UnsupportedOperationException(e));
	    }
	}

	private static Algorithm alg(String name, int dsz, int bsz) {
	    return(new Algorithm() {
		    public Digest get() {
			try {
			    return(new Builtin(MessageDigest.getInstance(name)));
			} catch(NoSuchAlgorithmException e) {
			    throw(new AssertionError(e));
			}
		    }

		    public int diglen() {return(dsz);}
		    public int blocklen() {return(bsz);}

		    public String toString() {return(name);}
		});
	}
    }

    public static class HMAC implements Digest {
	public final Algorithm dig;
	private final Digest inner, outer;
	private final byte[] key;

	private Digest addkey(Digest dig, byte mod) {
	    int bsz = this.dig.blocklen();
	    byte[] buf = Arrays.copyOf(key, ((key.length + bsz - 1) / bsz) * bsz);
	    for(int i = 0; i < buf.length; i++)
		buf[i] ^= mod;
	    dig.update(buf);
	    for(int i = 0; i < buf.length; i++)
		buf[i] ^= 0;
	    return(dig);
	}

	private HMAC(HMAC that) {
	    this.dig = that.dig;
	    this.key = that.key;
	    this.inner = that.inner.copy();
	    this.outer = that.outer.copy();
	}

	public HMAC(Algorithm dig, byte[] key) {
	    this.dig = dig;
	    this.key = key;
	    this.inner = addkey(dig.get(), (byte)0x36);
	    this.outer = addkey(dig.get(), (byte)0x5c);
	}

	public Digest update(byte[] part, int off, int len) {
	    inner.update(part, off, len);
	    return(this);
	}

	public byte[] digest() {
	    return(outer.update(inner.digest()).digest());
	}

	public Digest copy() {
	    return(new HMAC(this));
	}

	public static Algorithm alg(Algorithm dig, byte[] key) {
	    byte[] copy = Arrays.copyOf(key, key.length);
	    Algorithm ret = new Algorithm() {
		    public Digest get() {return(new HMAC(dig, copy));}
		    public int diglen() {return(dig.diglen());}
		    public int blocklen() {return(dig.blocklen());}
		};
	    Finalizer.finalize(ret, () -> Arrays.fill(copy, (byte)0));
	    return(ret);
	}
    }

    public static byte[] pbkdf2_hmac(Algorithm dig, byte[] pw, byte[] salt, int rounds, int len) {
	if(rounds < 1)
	    throw(new IllegalArgumentException(String.valueOf(rounds)));
	int bl = dig.diglen();
	byte[] ret = new byte[len];
	int r = salt.length;
	byte[] sbuf = Arrays.copyOf(salt, r + 4);
	for(int bc = 0; (bc * bl) < len; bc++) {
	    {
		int i = bc + 1;
		sbuf[r + 0] = (byte)((i & 0xff000000) >>> 24); sbuf[r + 1] = (byte)((i & 0x00ff0000) >>> 16);
		sbuf[r + 2] = (byte)((i & 0x0000ff00) >>>  8); sbuf[r + 3] = (byte)((i & 0x000000ff) >>>  0);
	    }
	    byte[] p = new HMAC(dig, pw).update(sbuf).digest();
	    byte[] blk = Arrays.copyOf(p, p.length);
	    for(int c = 1; c < rounds; c++) {
		byte[] n = new HMAC(dig, pw).update(p).digest();
		for(int i = 0; i < n.length; i++)
		    blk[i] ^= n[i];
		p = n;
	    }
	    System.arraycopy(blk, 0, ret, bc * bl, Math.min(bl, len - (bc * bl)));
	}
	return(ret);
    }
}
