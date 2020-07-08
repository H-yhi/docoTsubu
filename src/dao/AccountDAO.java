package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Account;
import model.Login;

public class AccountDAO {
	public Account findByLogic(Login login) {
		Connection conn = null;
		Account account = null;
		try {
			// JDBC ドライバを読み込む
			Class.forName("org.h2.Driver");

			// データベースに接続
			conn = DriverManager.getConnection("jdbc:h2:~/mydb", "sa", "sa");

			// SELECT文を準備
			String sql = "select user_id, pass, mail, name, age from account where user_id = ? and pass = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, login.getUserId());
			pStmt.setString(2, login.getPass());

			// SELECTを実行し、結果表を取得
			ResultSet rs = pStmt.executeQuery();

			// 一致したユーザーが存在した場合
			// そのユーザーを表すAccountインスタンスを生成
			if(rs.next()) {
				// 結果表からデータを取得
				String userId = rs.getString("user_id");
				String pass = rs.getString("pass");
				String mail = rs.getString("mail");
				String name = rs.getString("name");
				int age = rs.getInt("age");

				account = new Account(userId, pass, mail, name, age);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			// データベースを接続
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		// 見つけたユーザーまたはnullを返す
		return account;
	}
}
