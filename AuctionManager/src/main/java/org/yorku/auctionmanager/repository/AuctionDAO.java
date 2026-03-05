package org.yorku.auctionmanager.repository;

import org.springframework.stereotype.Repository;
import org.yorku.auctionmanager.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class AuctionDAO {
    
    // Example JDBC connection string for SQLite
    private static final String DB_URL = "jdbc:sqlite:auction_system.db";

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
		// TODO Auto-generated method stub
		return null;
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
}