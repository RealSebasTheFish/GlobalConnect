package org.yorku.paymenthandler.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import org.yorku.paymenthandler.model.PendingPayment;
import org.yorku.paymenthandler.model.Receipt;
import org.yorku.paymenthandler.model.UserPaymentMethod;

@Repository
public class PaymentDAO {
	private final DataSource dataSource;

	public PaymentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	private Connection conn() throws SQLException {
		return dataSource.getConnection();
	}

	public List<Receipt> fetchReceipts(int accountUID) throws SQLException {
		String sql = "SELECT account_uid, item_id, amount, payment_method, date, shipping_cost, expedited_shipping, expedited_extra_cost "
				+ "FROM receipts WHERE account_uid = ?";

		List<Receipt> result = new ArrayList<>();

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result.add(new Receipt(rs.getInt("account_uid"), rs.getInt("item_id"), rs.getDouble("amount"),
							rs.getString("payment_method"), rs.getString("date"), rs.getDouble("shipping_cost"),
							rs.getInt("expedited_shipping") != 0, rs.getDouble("expedited_extra_cost")));
				}
			}
		}

		return result;
	}

	public List<PendingPayment> fetchPending(int accountUID) throws SQLException {
		String sql = "SELECT account_uid, item_id FROM pending_payments WHERE account_uid = ?";

		List<PendingPayment> result = new ArrayList<>();

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result.add(new PendingPayment(rs.getInt("account_uid"), rs.getInt("item_id")));
				}
			}
		}

		return result;
	}

	public void addReceipt(Receipt r) throws SQLException {
		String sql = "INSERT INTO receipts(account_uid, item_id, amount, payment_method, date, shipping_cost, expedited_shipping, expedited_extra_cost) "
				+ "VALUES(?,?,?,?,?,?,?,?)";

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, r.getAccountUID());
			ps.setInt(2, r.getItemId());
			ps.setDouble(3, r.getAmount());
			ps.setString(4, r.getPaymentMethod());
			ps.setString(5, r.getDate());
			ps.setDouble(6, r.getShippingCost());
			ps.setInt(7, r.isExpeditedShipping() ? 1 : 0);
			ps.setDouble(8, r.getExpeditedExtraCost());

			ps.executeUpdate();
		}
	}

	public void addPending(int accountUID, int itemId) throws SQLException {
		String sql = "INSERT OR REPLACE INTO pending_payments(account_uid, item_id) VALUES(?,?)";

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);
			ps.setInt(2, itemId);

			ps.executeUpdate();
		}
	}

	public void removePending(int accountUID, int itemId) throws SQLException {
		String sql = "DELETE FROM pending_payments WHERE account_uid = ? AND item_id = ?";

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);
			ps.setInt(2, itemId);

			int affected = ps.executeUpdate();

			if (affected == 0)
				throw new SQLException("Not found");
		}
	}

	public boolean pendingExists(int accountUID, int itemId) throws SQLException {
		String sql = "SELECT 1 FROM pending_payments WHERE account_uid = ? AND item_id = ? LIMIT 1";

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);
			ps.setInt(2, itemId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public boolean receiptExists(int accountUID, int itemId) throws SQLException {
		String sql = "SELECT 1 FROM receipts WHERE account_uid = ? AND item_id = ? LIMIT 1";

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);
			ps.setInt(2, itemId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public void upsertPaymentMethod(UserPaymentMethod m) throws SQLException {
		String sql = "INSERT OR REPLACE INTO payment_methods(account_uid, payment_method, cardholder_name, last4, exp_date) VALUES(?,?,?,?,?)";

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, m.getAccountUID());
			ps.setString(2, m.getPaymentMethod());
			ps.setString(3, m.getCardholderName());
			ps.setString(4, m.getLast4());
			ps.setString(5, m.getExpDate());

			ps.executeUpdate();
		}
	}

	public List<UserPaymentMethod> fetchPaymentMethods(int accountUID) throws SQLException {
		String sql = "SELECT account_uid, payment_method, cardholder_name, last4, exp_date FROM payment_methods WHERE account_uid = ?";

		List<UserPaymentMethod> out = new ArrayList<>();

		try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, accountUID);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					out.add(new UserPaymentMethod(rs.getInt("account_uid"), rs.getString("payment_method"),
							rs.getString("cardholder_name"), rs.getString("last4"), rs.getString("exp_date")));
				}
			}
		}

		return out;
	}
}