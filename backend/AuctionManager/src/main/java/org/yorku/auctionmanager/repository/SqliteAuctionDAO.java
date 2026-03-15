package org.yorku.auctionmanager.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.yorku.auctionmanager.model.Item;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SqliteAuctionDAO {

    private final JdbcTemplate jdbc;

    // Spring Boot automatically injects the connection from application.properties here!
    public SqliteAuctionDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean insertItem(Item item) {
        try {
            KeyHolder kh = new GeneratedKeyHolder();
            
            int rowsAffected = jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO items (owner_uid, name, description, starting_price, current_highest_bid, highest_bidder_uid, is_closed) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1, item.getOwnerUid());
                ps.setString(2, item.getName());
                ps.setString(3, item.getDescription());
                ps.setDouble(4, item.getStartingPrice());
                ps.setDouble(5, item.getStartingPrice()); // Initial bid is starting price
                ps.setInt(6, -1); // -1 to indicate no bidder yet
                ps.setInt(7, 0);  // 0 means false
                return ps;
            }, kh);

            if (rowsAffected > 0 && kh.getKey() != null) {
                item.setId(kh.getKey().intValue());
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error inserting item: " + e.getMessage());
            return false;
        }
    }

    public List<Item> fetchAllItems() {
        return jdbc.query(
            "SELECT * FROM items WHERE is_closed = 0",
            (rs, rowNum) -> mapRowToItem(rs)
        );
    }

    public Item fetchItemById(int itemId) {
        List<Item> items = jdbc.query(
            "SELECT * FROM items WHERE id = ?",
            (rs, rowNum) -> mapRowToItem(rs),
            itemId
        );
        return items.isEmpty() ? null : items.get(0);
    }

    // Missing method from previous commit!
    public List<Item> fetchItemsByOwner(int ownerUid) {
        return jdbc.query(
            "SELECT * FROM items WHERE owner_uid = ?",
            (rs, rowNum) -> mapRowToItem(rs),
            ownerUid
        );
    }

    public boolean updateItem(Item item) {
        try {
            int rowsAffected = jdbc.update(
                "UPDATE items SET name = ?, description = ?, starting_price = ?, current_highest_bid = ?, highest_bidder_uid = ?, is_closed = ? WHERE id = ?",
                item.getName(),
                item.getDescription(),
                item.getStartingPrice(),
                item.getCurrentHighestBid(),
                item.getHighestBidderUid(),
                item.isClosed() ? 1 : 0,
                item.getId()
            );
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error updating item: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        try {
            // Note: Our AuctionDatabaseManager already checks the ownerUid permission before calling this,
            // so we only need the itemId here.
            int rowsAffected = jdbc.update("DELETE FROM items WHERE id = ?", itemId);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error deleting item: " + e.getMessage());
            return false;
        }
    }

    // Helper method mapping ResultSet to Item (No explicit while loops needed!)
    private Item mapRowToItem(java.sql.ResultSet rs) throws java.sql.SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setOwnerUid(rs.getInt("owner_uid"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setStartingPrice(rs.getDouble("starting_price"));
        item.setCurrentHighestBid(rs.getDouble("current_highest_bid"));
        item.setHighestBidderUid(rs.getInt("highest_bidder_uid"));
        item.setClosed(rs.getInt("is_closed") == 1);
        return item;
    }
}