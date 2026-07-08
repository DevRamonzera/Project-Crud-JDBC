package com.br.project_crud_jdbc.repository;

import com.br.project_crud_jdbc.conn.ConnectionFactory;
import com.br.project_crud_jdbc.domain.Trainer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class TrainerRepository {
    public static void save(Trainer trainer)throws SQLException{
        log.info("Saving trainer '{}'...", trainer);
        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement pstmt = createPreparedStatementSave(conn, trainer)){
            List<Trainer> trainers = findAll();
            boolean exist = trainers.stream()
                    .anyMatch(t ->t.getName().equalsIgnoreCase(trainer.getName()));
            if(exist){
                log.error("Trainer already exists");
            }
            int rowsAffected = pstmt.executeUpdate();
            log.info("Trainer '{}' saved in the database", trainer.getName());
        }catch(SQLException e){
            log.error("Error while saving trainer", e);
        }
    }
    public static PreparedStatement createPreparedStatementSave(Connection conn, Trainer trainer)throws SQLException{
        String sql = "INSERT INTO `pokedex`.`trainer` (`name`) VALUES (?);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, trainer.getName());
        return pstmt;
    }

    public static void update(Trainer trainer){
        log.info("Updating trainer '{}'...", trainer);
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPrepareStatementUpdate(conn, trainer)){
            int rowsAffected = pstmt.executeUpdate();
            log.info("Trainer '{}' updated successfully. Rows affected: {}", trainer.getName(), rowsAffected);
        }catch(SQLException e){
            log.error("Error while updating trainer '{}'", trainer.getName(), e);
        }
    }
    private static PreparedStatement createPrepareStatementUpdate(Connection conn, Trainer trainer) throws SQLException {
        String sql = "UPDATE `pokedex`.`trainer` SET `name` = ? WHERE (`id` = ?);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, trainer.getName());
        pstmt.setInt(2, trainer.getId());
        return pstmt;
    }

    public static void delete(Integer id) throws SQLException{
        log.info("Deleting trainer with id '{}'...", id);
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPreparedStatementDelete(conn, id)){
            int rowsAffected = pstmt.executeUpdate();
            log.info("Trainer with id '{}' deleted successfully. Rows affected: '{}'", id, rowsAffected);
        }catch(SQLException e){
            log.error("Error while deleting trainer with id '{}'", id, e);
        }
    }
    private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id) throws SQLException{
        String sql = "DELETE FROM `pokedex`.`trainer` WHERE (`id` = ?);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt;
    }

    public static List<Trainer> findByName(String name) throws SQLException{
        log.info("Finding trainer by name...");
        List<Trainer> trainers = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement psmt = createPreparedStatementFindByName(conn, name);
            ResultSet rs = psmt.executeQuery()){
            while(rs.next()){
                Trainer trainer = Trainer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
                trainers.add(trainer);
            }
        }catch(SQLException e){
            log.error("Error when trying to find trainer by name", e);
        }
        return trainers;
    }
    public static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT * FROM pokedex.trainer where name like ?;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        return pstmt;
    }

    public static Optional<Trainer> findById(Integer id) throws SQLException{
        log.info("Finding trainer by id '{}'...", id);
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPreparedStatementFindById(conn, id);
            ResultSet rs = pstmt.executeQuery()) {
            if(!rs.next()) return Optional.empty();
            return Optional.of(Trainer.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build());
        }catch(SQLException e){
            log.error("Error while trying find trainer by id '{}'", id, e);
        }
        return Optional.empty();
    }
    public static PreparedStatement createPreparedStatementFindById(Connection conn, Integer id) throws SQLException{
        String sql = "SELECT * FROM pokedex.trainer where id = ?;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt;
    }

    public static List<Trainer> findAll()throws SQLException{
        log.info("Finding all trainers...");
        List<Trainer> trainers = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPreparedStatementFindAll(conn);
            ResultSet rs = pstmt.executeQuery();){
            while(rs.next()){
               Trainer trainer = Trainer.builder()
                       .id(rs.getInt("id"))
                       .name(rs.getString("name"))
                       .build();
               trainers.add(trainer);
            }
        }catch(SQLException e){
            log.error("Error while trying find all trainers", e);
        }
        return trainers;
    }
    public static PreparedStatement createPreparedStatementFindAll(Connection conn) throws SQLException{
        String sql = "SELECT id, name FROM pokedex.trainer;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt;
    }
}
