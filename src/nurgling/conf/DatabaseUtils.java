import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    // URL базы данных (замените на ваш)
    private static final String URL = "jdbc:postgresql://<host>:<port>/<database>";
    private static final String USER = "<username>";  // Имя пользователя БД
    private static final String PASSWORD = "<password>";  // Пароль

    // Метод для получения соединения с базой данных
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Пример метода для добавления хранилища в базу данных
    public static int addStorage(String zoneUUID, double coordX, double coordY, String type) {
        String sql = "INSERT INTO storage (uuid, coord_x, coord_y, type, last_updated) VALUES (?, ?, ?, ?, NOW()) RETURNING id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, zoneUUID);
            stmt.setDouble(2, coordX);
            stmt.setDouble(3, coordY);
            stmt.setString(4, type);

            // Выполняем запрос и возвращаем сгенерированный ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);  // Возвращаем сгенерированный ID хранилища
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // В случае ошибки
    }

    // Пример метода для получения предметов из хранилища
    public static void getItemsInStorage(int storageId) {
        String sql = "SELECT item_name, quality, quantity FROM items_in_storage WHERE storage_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, storageId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                double quality = rs.getDouble("quality");
                int quantity = rs.getInt("quantity");

                System.out.println("Item: " + itemName + ", Quality: " + quality + ", Quantity: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
