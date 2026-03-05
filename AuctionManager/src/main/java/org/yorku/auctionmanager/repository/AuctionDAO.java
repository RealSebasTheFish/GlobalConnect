package org.yorku.auctionmanager.repository;

import org.springframework.stereotype.Repository;
import org.yorku.auctionmanager.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuctionDAO {
    
    // Just the file name. No absolute paths, no ::resource:: tags.
    private static final String DB_URL = "jdbc:sqlite:auction_db.db";

    public Item insertItem(Item item) {
        // Note we include all the fields now
        String sql = "INSERT INTO items(owner_uid, name, description, starting_price, current_highest_bid, highest_bidder_uid, is_closed) VALUES(?,?,?,?,?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             // This second parameter is the magic key to getting the ID back!
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, item.getOwnerUid());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setDouble(4, item.getStartingPrice());
            pstmt.setDouble(5, item.getStartingPrice()); // Initial bid is the starting price
            pstmt.setInt(6, -1); // -1 or 0 to indicate no bidder yet
            pstmt.setInt(7, 0);  // 0 means false (not closed)
            
            pstmt.executeUpdate();
            
            // Retrieve the newly created ID from SQLite
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1)); // Set the ID on our Java object
                }
            }
            return item; // Return the fully populated object!
            
        } catch (SQLException e) {
            System.out.println("Error inserting item: " + e.getMessage());
            return null; // Return null if it fails
        }
    }

    public List<Item> fetchAllItems() {
        List<Item> catalogue = new ArrayList<>();
        // We only fetch items where is_closed = 0 (meaning the auction is still active)
        String sql = "SELECT * FROM items WHERE is_closed = 0";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop through every row and convert it to an Item
            while (rs.next()) {
                catalogue.add(mapRowToItem(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching catalogue: " + e.getMessage());
        }
        
        return catalogue;
    }
	
	public boolean deleteItem(int itemId, int ownerUid) {
	    // We include owner_uid in the WHERE clause as a strict security measure
	    // This ensures a user can ONLY delete an item if they are the true owner.
	    String sql = "DELETE FROM items WHERE id = ? AND owner_uid = ?";
	    
	    try (Connection conn = DriverManager.getConnection(DB_URL);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        // 1. Set the parameters
	        pstmt.setInt(1, itemId);
	        pstmt.setInt(2, ownerUid);
	        
	        // 2. Execute the delete query
	        int rowsAffected = pstmt.executeUpdate();
	        
	        // 3. If rowsAffected is greater than 0, the delete was successful!
	        return rowsAffected > 0;
	        
	    } catch (SQLException e) {
	        System.out.println("Error deleting item: " + e.getMessage());
	        return false;
	    }
	}

    // I will add updateItem, findById, and findAll methods here
	public Item fetchItemById(int itemId) {
        String sql = "SELECT * FROM items WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // If a row is found, map it to an Item object and return it
                if (rs.next()) {
                    return mapRowToItem(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching item by ID: " + e.getMessage());
        }
        
        // Return null if the item doesn't exist or an error occurred
        return null; 
    }
	// Helper method to convert a database row into a Java Object
    private Item mapRowToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setOwnerUid(rs.getInt("owner_uid"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setStartingPrice(rs.getDouble("starting_price"));
        item.setCurrentHighestBid(rs.getDouble("current_highest_bid"));
        item.setHighestBidderUid(rs.getInt("highest_bidder_uid"));
        
        // SQLite doesn't have a strict boolean, so we read 1 as true, 0 as false
        item.setClosed(rs.getInt("is_closed") == 1); 
        
        return item;
    }
    
    public List<Item> fetchItemsByOwner(int ownerUid) {
        List<Item> userItems = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE owner_uid = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ownerUid);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // Loop through all results and add them to the list
                while (rs.next()) {
                    userItems.add(mapRowToItem(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user's items: " + e.getMessage());
        }
        
        return userItems;
    }
    
    public boolean updateItem(Item item) {
        // We update all modifiable fields based on the item's ID
        String sql = "UPDATE items SET name = ?, description = ?, starting_price = ?, " +
                     "current_highest_bid = ?, highest_bidder_uid = ?, is_closed = ? " +
                     "WHERE id = ?";
                     
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setDouble(3, item.getStartingPrice());
            pstmt.setDouble(4, item.getCurrentHighestBid());
            pstmt.setInt(5, item.getHighestBidderUid());
            
            // SQLite doesn't have a strict boolean type, so we store 1 for true, 0 for false
            pstmt.setInt(6, item.isClosed() ? 1 : 0); 
            
            pstmt.setInt(7, item.getId()); // The WHERE clause ID
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating item: " + e.getMessage());
            return false;
        }
    }
}








