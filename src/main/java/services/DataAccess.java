/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package services;
import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

public class DataAccess {
    public DataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String alloyURI = System.getenv("alloydb_uri");
        String user = System.getenv("alloydb_user");
        String pass = System.getenv("alloydb_password");
        String db = System.getenv("alloydb_db");
        dataSource.setServerNames(new String[]{alloyURI});
        dataSource.setDatabaseName(db);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        return dataSource;
    }

    public Map<String, Object> findBook(String title) throws SQLException {
        String sql = "SELECT * FROM books WHERE UPPER(title) = UPPER(?)";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Map.of("book_id", rs.getInt("book_id"));
                }
            }
        }
        return new HashMap<>();
    }

    public Map<String, Object> findAuthor(String authorName) throws SQLException {
        String sql = "SELECT * FROM authors WHERE name = ?";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authorName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Map.of("author_id", rs.getInt("author_id"));
                }
            }
        }
        return new HashMap<>();
    }

    public Integer insertAuthor(String bio, String author) throws SQLException {
        String sql = "INSERT INTO authors (bio, name) VALUES (?, ?) RETURNING author_id";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bio);
            stmt.setString(2, author);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("author_id");
                }
            }
        }
        return null;
    }

    public Integer insertBook(Integer authorId, String title, String year, ScopeType publicPrivate) throws SQLException {
        LocalDate publicationYear = LocalDate.parse(year);
        String sql = "INSERT INTO books (author_id, publication_year, title, scope) VALUES (?, ?, ?, CAST(? AS scope_type)) RETURNING book_id";
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.setObject(2, publicationYear);
            stmt.setString(3, title);
            stmt.setString(4, publicPrivate.getValue());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("book_id");
                }
            }
        }
        return null;
    }
}