/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecoride.ecoride.dao;

import com.ecoride.ecoride.model.ElectricBike;
import com.ecoride.ecoride.model.ElectricScooter;
import com.ecoride.ecoride.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * VehicleDAO — Data Access Object untuk Vehicle
 *
 * Skema DB:
 *   vehicles         : vehicle_id (PK varchar), model, battery_level, is_available, vehicle_type
 *   electric_bikes   : vehicle_id (FK), has_pedals, price_per_minute
 *   electric_scooters: vehicle_id (FK), max_speed, price_per_minute
 *
 * Getter model yang dipakai (sesuai Vehicle.java):
 *   getVehicleID()      → PK string kendaraan
 *   getRentPerMinute()  → tarif per menit
 *   getBatteryLevel()   → level baterai
 *   isAvailable()       → status ketersediaan
 */
public class VehicleDAO {

    // =========================================================
    // SQL base — JOIN ke sub-tabel sekaligus
    // COALESCE supaya price_per_minute selalu terisi
    // =========================================================
    private static final String SELECT_ALL =
        "SELECT v.vehicle_id, v.model, v.battery_level, v.is_available, v.vehicle_type, "
      + "eb.has_pedals, "
      + "COALESCE(eb.price_per_minute, es.price_per_minute) AS price_per_minute, "
      + "es.max_speed "
      + "FROM vehicles v "
      + "LEFT JOIN electric_bikes    eb ON v.vehicle_id = eb.vehicle_id "
      + "LEFT JOIN electric_scooters es ON v.vehicle_id = es.vehicle_id";

    // =========================================================
    // GET ALL AVAILABLE — untuk member
    // =========================================================
    public List<Vehicle> getAllAvailable() {
        List<Vehicle> list = new ArrayList<Vehicle>();
        String sql = SELECT_ALL
                   + " WHERE v.is_available = 1 AND v.battery_level > 10"
                   + " ORDER BY v.vehicle_type, v.vehicle_id";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }
            System.out.println("[VehicleDAO] getAllAvailable() — ditemukan: " + list.size());
        } catch (SQLException e) {
            System.err.println("[VehicleDAO] getAllAvailable() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return list;
    }

    // =========================================================
    // GET ALL — untuk admin (semua kendaraan)
    // =========================================================
    public List<Vehicle> getAll() {
        List<Vehicle> list = new ArrayList<Vehicle>();
        String sql = SELECT_ALL + " ORDER BY v.vehicle_type, v.vehicle_id";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }
            System.out.println("[VehicleDAO] getAll() — ditemukan: " + list.size());
        } catch (SQLException e) {
            System.err.println("[VehicleDAO] getAll() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return list;
    }

    // =========================================================
    // FIND BY VEHICLE_ID — dipakai RentServlet
    // =========================================================
    public Vehicle findByVehicleId(String vehicleId) {
        String sql = SELECT_ALL + " WHERE v.vehicle_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setString(1, vehicleId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Vehicle v = mapRowToVehicle(rs);
                System.out.println("[VehicleDAO] findByVehicleId(" + vehicleId + ") — " + v.getModel());
                return v;
            } else {
                System.err.println("[VehicleDAO] findByVehicleId() — tidak ditemukan: " + vehicleId);
            }
        } catch (SQLException e) {
            System.err.println("[VehicleDAO] findByVehicleId() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(rs, ps, conn);
        }
        return null;
    }

    // =========================================================
    // UPDATE AVAILABILITY
    // =========================================================
    public boolean updateAvailability(String vehicleId, boolean available) {
        String sql = "UPDATE vehicles SET is_available = ? WHERE vehicle_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setBoolean(1, available);
            ps.setString(2, vehicleId);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[VehicleDAO] updateAvailability(" + vehicleId
                    + ", " + available + ") — " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[VehicleDAO] updateAvailability() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // UPDATE BATTERY — dipanggil AdminServlet.doChargeVehicle()
    // =========================================================
    public boolean updateBattery(String vehicleId, double batteryLevel) {
        String sql = "UPDATE vehicles SET battery_level = ? WHERE vehicle_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setDouble(1, batteryLevel);
            ps.setString(2, vehicleId);
            boolean ok = ps.executeUpdate() > 0;
            System.out.println("[VehicleDAO] updateBattery(" + vehicleId
                    + ", " + batteryLevel + "%) — " + (ok ? "OK" : "GAGAL"));
            return ok;
        } catch (SQLException e) {
            System.err.println("[VehicleDAO] updateBattery() error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // ADD VEHICLE — dipanggil AdminServlet.doAddVehicle()
    // Pakai getter sesuai Vehicle.java:
    //   getVehicleID()     (huruf besar ID)
    //   getRentPerMinute() (bukan getPricePerMinute)
    // =========================================================
    public boolean addVehicle(Vehicle vehicle) {
        String sqlV = "INSERT INTO vehicles "
                    + "(vehicle_id, model, vehicle_type, battery_level, is_available) "
                    + "VALUES (?, ?, ?, ?, 1)";

        Connection conn   = null;
        PreparedStatement psV = null;
        PreparedStatement psD = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            psV = conn.prepareStatement(sqlV);
            psV.setString(1, vehicle.getVehicleID());
            psV.setString(2, vehicle.getModel());
            psV.setDouble(4, vehicle.getBatteryLevel());

            if (vehicle instanceof ElectricBike) {
                ElectricBike bike = (ElectricBike) vehicle;
                psV.setString(3, "BIKE");
                psV.executeUpdate();

                psD = conn.prepareStatement(
                    "INSERT INTO electric_bikes (vehicle_id, has_pedals, price_per_minute) "
                  + "VALUES (?, ?, ?)");
                psD.setString(1, bike.getVehicleID());
                psD.setBoolean(2, bike.isHasPedals());
                psD.setDouble(3, bike.getRentPerMinute());
                psD.executeUpdate();

            } else if (vehicle instanceof ElectricScooter) {
                ElectricScooter scooter = (ElectricScooter) vehicle;
                psV.setString(3, "SCOOTER");
                psV.executeUpdate();

                psD = conn.prepareStatement(
                    "INSERT INTO electric_scooters (vehicle_id, max_speed, price_per_minute) "
                  + "VALUES (?, ?, ?)");
                psD.setString(1, scooter.getVehicleID());
                psD.setInt(2, scooter.getMaxSpeed());
                psD.setDouble(3, scooter.getRentPerMinute());
                psD.executeUpdate();
            }

            conn.commit();
            System.out.println("[VehicleDAO] addVehicle() berhasil: " + vehicle.getVehicleID());
            return true;

        } catch (SQLException e) {
            System.err.println("[VehicleDAO] addVehicle() error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            tutup(null, psD, null);
            tutup(null, psV, conn);
        }
        return false;
    }

    // =========================================================
    // DELETE — dipanggil AdminServlet.doDeleteVehicle()
    // Hapus sub-tabel dulu sebelum hapus vehicles
    // =========================================================
    public boolean delete(String vehicleId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Urutan hapus: transactions → bikes/scooters → vehicles
            // (ikuti urutan FK agar tidak error)
            String[] subTables = {
                "rental_transactions",
                "electric_bikes",
                "electric_scooters"
            };
            for (String tbl : subTables) {
                ps = conn.prepareStatement("DELETE FROM " + tbl + " WHERE vehicle_id = ?");
                ps.setString(1, vehicleId);
                ps.executeUpdate();
                ps.close();
                ps = null;
            }

            // Hapus dari vehicles
            ps = conn.prepareStatement("DELETE FROM vehicles WHERE vehicle_id = ?");
            ps.setString(1, vehicleId);
            int rows = ps.executeUpdate();

            conn.commit();
            System.out.println("[VehicleDAO] delete(" + vehicleId + ") — "
                    + (rows > 0 ? "OK" : "tidak ditemukan"));
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[VehicleDAO] delete() error: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            tutup(null, ps, conn);
        }
        return false;
    }

    // =========================================================
    // Helper — mapping ResultSet ke ElectricBike / ElectricScooter
    //
    // Constructor ElectricBike  : (vehicleID, model, batteryLevel, isAvailable, rentPerMinute, hasPedals)
    // Constructor ElectricScooter: (vehicleID, model, batteryLevel, isAvailable, rentPerMinute, maxSpeed)
    // =========================================================
    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        String  vehicleId   = rs.getString("vehicle_id");
        String  model       = rs.getString("model");
        double  battery     = rs.getDouble("battery_level");
        boolean available   = rs.getBoolean("is_available");
        double  rentPerMin  = rs.getDouble("price_per_minute");
        String  type        = rs.getString("vehicle_type");

        System.out.println("[VehicleDAO] mapRowToVehicle() — id=" + vehicleId
                + " type=" + type + " rentPerMin=" + rentPerMin);

        // equalsIgnoreCase aman dari "BIKE" / "bike" / "Bike"
        if ("BIKE".equalsIgnoreCase(type)) {
            boolean hasPedals = rs.getBoolean("has_pedals");
            // constructor: (vehicleID, model, batteryLevel, isAvailable, rentPerMinute, hasPedals)
            return new ElectricBike(vehicleId, model, battery, available, rentPerMin, hasPedals);
        } else {
            int maxSpeed = rs.getInt("max_speed");
            // constructor: (vehicleID, model, batteryLevel, isAvailable, rentPerMinute, maxSpeed)
            return new ElectricScooter(vehicleId, model, battery, available, rentPerMin, maxSpeed);
        }
    }

    // =========================================================
    // Helper — tutup resource JDBC secara aman
    // =========================================================
    private void tutup(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try { rs.close(); }
            catch (SQLException e) { System.err.println("[VehicleDAO] tutup rs: " + e.getMessage()); }
        }
        if (ps != null) {
            try { ps.close(); }
            catch (SQLException e) { System.err.println("[VehicleDAO] tutup ps: " + e.getMessage()); }
        }
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { System.err.println("[VehicleDAO] tutup conn: " + e.getMessage()); }
        }
    }
}
