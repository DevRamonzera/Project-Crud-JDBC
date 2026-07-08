package com.br.project_crud_jdbc.repository;

import com.br.project_crud_jdbc.conn.ConnectionFactory;
import com.br.project_crud_jdbc.domain.Pokemon;
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
public class PokemonRepository {
    public static void save(Pokemon pokemon)throws SQLException{
        log.info("Saving pokemon '{}'...", pokemon);
        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement pstmt = createPreparedStatementSave(conn, pokemon)){
            int rowsAffected = pstmt.executeUpdate();
            log.info("Pokemon '{}' saved in the database", pokemon.getName());
        }catch(SQLException e){
            log.error("Error while saving pokemon", e);
        }
    }
    public static PreparedStatement createPreparedStatementSave(Connection conn, Pokemon pokemon)throws SQLException{
        String sql = "INSERT INTO `pokedex`.`pokemon` (`name`, `type`, `trainer_id`) VALUES ( ?, ?, ?);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, pokemon.getName());
        pstmt.setString(2, pokemon.getType());
        pstmt.setInt(3, pokemon.getTrainer().getId());
        return pstmt;
    }

    public static void update(Pokemon pokemon){
        log.info("Updating pokemon '{}'...", pokemon);
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPrepareStatementUpdate(conn, pokemon)){
            int rowsAffected = pstmt.executeUpdate();
            log.info("Pokemon '{}' updated successfully. Rows affected: {}", pokemon.getName(), rowsAffected);
        }catch(SQLException e){
            log.error("Error while updating pokemon '{}'", pokemon.getName(), e);
        }
    }
    private static PreparedStatement createPrepareStatementUpdate(Connection conn, Pokemon pokemon) throws SQLException {
        String sql = "UPDATE `pokedex`.`pokemon` SET `name` = ?, `type` = ? WHERE (`id` = ?);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, pokemon.getName());
        pstmt.setString(2, pokemon.getType());
        pstmt.setInt(3, pokemon.getId());
        return pstmt;
    }

    public static void delete(Integer id) throws SQLException{
        log.info("Deleting pokemon with id '{}'...", id);
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPreparedStatementDelete(conn, id)){
            int rowsAffected = pstmt.executeUpdate();
            log.info("Pokemon with id '{}' deleted successfully. Rows affected: '{}'", id, rowsAffected);
        }catch(SQLException e){
            log.error("Error while deleting pokemon with id '{}'", id, e);
        }
    }
    private static PreparedStatement createPreparedStatementDelete(Connection conn, Integer id) throws SQLException{
        String sql = "DELETE FROM `pokedex`.`pokemon` WHERE (`id` = ?);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt;
    }

    public static List<Pokemon> findByName(String name) throws SQLException{
        log.info("Finding pokemon by name...");
        List<Pokemon> pokemons = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement psmt = createPreparedStatementFindByName(conn, name);
            ResultSet rs = psmt.executeQuery()){
            while(rs.next()){
                Trainer trainer = Trainer.builder()
                        .id(rs.getInt("trainer_id"))
                        .name(rs.getString("trainer_name"))
                        .build();

                Pokemon pokemon = Pokemon.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .type(rs.getString("type"))
                        .trainer(trainer)
                        .build();
                pokemons.add(pokemon);
            }
        }catch(SQLException e){
            log.error("Error when trying to find pokemon by name", e);
        }
        return pokemons;
    }
    public static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = """
                SELECT p.id, p.name, p.type, p.trainer_id, t.name as 'trainer_name' FROM pokedex.pokemon p inner join
                pokedex.trainer t on p.trainer_id = t.id
                where p.name like ?;
                """;
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        return pstmt;
    }

    public static Optional<Pokemon> findById(Integer id) throws SQLException{
        log.info("Finding pokemon by id '{}'...", id);
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPreparedStatementFindById(conn, id);
            ResultSet rs = pstmt.executeQuery()) {
            if(!rs.next()) return Optional.empty();
            Trainer trainer = Trainer.builder()
                    .id(rs.getInt("trainer_id"))
                    .name(rs.getString("trainer_name"))
                    .build();
            Pokemon pokemon = Pokemon.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .type(rs.getString("type"))
                    .trainer(trainer)
                    .build();
            return Optional.of(pokemon);
        }catch(SQLException e){
            log.error("Error while trying find pokemon by id '{}'", id, e);
        }
        return Optional.empty();
    }
    public static PreparedStatement createPreparedStatementFindById(Connection conn, Integer id) throws SQLException{
        String sql = """
                SELECT p.id, p.name, p.type, p.trainer_id, t.name as 'trainer_name' FROM pokedex.pokemon p inner join
                pokedex.trainer t on p.trainer_id = t.id
                where p.id = ?;
                """;
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        return pstmt;
    }

    public static List<Pokemon> findAll()throws SQLException{
        log.info("Finding all pokemons...");
        List<Pokemon> pokemons = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement pstmt = createPreparedStatementFindAll(conn);
            ResultSet rs = pstmt.executeQuery();){
            while(rs.next()){
                Trainer trainer = Trainer.builder()
                        .id(rs.getInt("trainer_id"))
                        .name(rs.getString("trainer_name"))
                        .build();
               Pokemon pokemon = Pokemon.builder()
                       .id(rs.getInt("id"))
                       .name(rs.getString("name"))
                       .type(rs.getString("type"))
                       .trainer(trainer)
                       .build();
               pokemons.add(pokemon);
            }
        }catch(SQLException e){
            log.error("Error while trying find all pokemons", e);
        }
        return pokemons;
    }
    public static PreparedStatement createPreparedStatementFindAll(Connection conn) throws SQLException{
        String sql = """
                SELECT p.id,
                p.name,
                p.type,
                t.id AS trainer_id,
                t.name AS trainer_name
                FROM pokedex.pokemon p
                INNER JOIN pokedex.trainer t
                ON p.trainer_id = t.id;""";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt;
    }
}
