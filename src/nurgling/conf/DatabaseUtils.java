package nurgling.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    // URL базы данных (замените на ваш)
    //private static final String URL = "jdbc:postgresql://postgres:qrNtYOAnnPRiCeEoQJLAjKAjdqMCPzYD@junction.proxy.rlwy.net:38959/railway";
    private static final String URL = "jdbc:postgresql://junction.proxy.rlwy.net:38959/railway?user=postgres&password=qrNtYOAnnPRiCeEoQJLAjKAjdqMCPzYD";
    private static final String USER = "postgres";  // Имя пользователя БД
    private static final String PASSWORD = "qrNtYOAnnPRiCeEoQJLAjKAjdqMCPzYD";  // Пароль

    // Метод для получения соединения с базой данных
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void createTables() {
        String createStorageTable = "CREATE TABLE IF NOT EXISTS storage (" +
                "id SERIAL PRIMARY KEY, " +
                "uuid VARCHAR(36) NOT NULL, " +
                "coord_x DOUBLE PRECISION NOT NULL, " +
                "coord_y DOUBLE PRECISION NOT NULL, " +
                "type VARCHAR(255) NOT NULL, " +
                "last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String createItemsTable = "CREATE TABLE IF NOT EXISTS items_in_storage (" +
                "id SERIAL PRIMARY KEY, " +
                "storage_id INTEGER NOT NULL REFERENCES storage(id) ON DELETE CASCADE, " +
                "item_name VARCHAR(255) NOT NULL, " +
                "quality DOUBLE PRECISION NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = getConnection();
             PreparedStatement createStorageStmt = conn.prepareStatement(createStorageTable);
             PreparedStatement createItemsStmt = conn.prepareStatement(createItemsTable)) {

            // Выполняем запросы на создание таблиц
            createStorageStmt.executeUpdate();
            createItemsStmt.executeUpdate();

            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Метод для добавления 64 предметов в базу данных с замером времени
    public static void addItemsWithTiming(int storageId) {
        String sql = "INSERT INTO items_in_storage (storage_id, item_name, quality, quantity) VALUES (?, ?, ?, ?)";

        long startTime = System.currentTimeMillis();  // Старт замера времени

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 256; i++) {
                stmt.setInt(1, storageId);  // Указываем идентификатор хранилища
                stmt.setString(2, "Item_" + i);   // Имя предмета (Item_1, Item_2, ...)
                stmt.setDouble(3, Math.random() * 100);  // Случайное качество от 0 до 100
                stmt.setInt(4, (int) (Math.random() * 50) + 1);  // Случайное количество от 1 до 50
                stmt.addBatch();  // Добавляем в пакет
            }

            // Выполняем пакетный запрос
            stmt.executeBatch();

            long endTime = System.currentTimeMillis();  // Конец замера времени
            long executionTime = endTime - startTime;  // Разница во времени

            System.out.println("64 items added successfully.");
            System.out.println("Execution time: " + executionTime + " ms");

        } catch (SQLException e) {
            e.printStackTrace();
        }
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