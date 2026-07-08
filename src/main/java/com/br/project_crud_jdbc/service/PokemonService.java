package com.br.project_crud_jdbc.service;

import com.br.project_crud_jdbc.domain.Pokemon;
import com.br.project_crud_jdbc.domain.Trainer;
import com.br.project_crud_jdbc.repository.PokemonRepository;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Log4j2
public class PokemonService {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu(int op) throws SQLException {
        switch (op) {
            case 1 -> save();
            case 2 -> findByName();
            case 3 -> findAll();
            case 4 -> update();
            case 5 -> deleted();
        }
    }

    private static void save() throws SQLException{
        System.out.println("Write the name of pokemon that want save");
        String name = scanner.nextLine();
        System.out.println("Write the type of pokemon that want save");
        String type = scanner.nextLine();
        System.out.println("Write the id of pokemon trainer that want save");
        Integer trainerId = Integer.parseInt(scanner.nextLine());
        Pokemon pokemon = Pokemon.builder()
                .name(name)
                .type(type)
                .trainer(Trainer.builder().id(trainerId).build())
                .build();
        PokemonRepository.save(pokemon);
    }

    private static void deleted() throws SQLException{
        findAll();
        System.out.println("Write the id of pokemon that want delete");
        Integer id = Integer.parseInt(scanner.nextLine());
        System.out.println("Are you sure? S/N");
        String choice = scanner.nextLine();
        if("s".equalsIgnoreCase(choice)){
            PokemonRepository.delete(id);
        }
        if("n".equalsIgnoreCase(choice)){
            log.info("Action was cancelled");
        }
    }

    public static void update() throws SQLException{
        findAll();
        System.out.println("Write the id of the pokemon that want to update");
        Optional<Pokemon> pokemonOptional = PokemonRepository.findById(Integer.parseInt(scanner.nextLine()));
        if(pokemonOptional.isEmpty()){
            log.info("Pokemon not found");
            return;
        }
        Pokemon pokemonFromDatabase = pokemonOptional.get();
        System.out.println("Write the new name or enter to keep the same");
        String name = scanner.nextLine();
        System.out.println("Write the new type or enter to keep the same");
        String type = scanner.nextLine();

        name = name.isEmpty() ? pokemonFromDatabase.getName() : name;
        type = type.isEmpty() ? pokemonFromDatabase.getType() : type;

        Pokemon pokemonUpdate = Pokemon.builder()
                .id(pokemonFromDatabase.getId())
                .name(name)
                .type(type)
                .trainer(pokemonFromDatabase.getTrainer())
                .build();
        PokemonRepository.update(pokemonUpdate);
    }

    private static void findByName() throws SQLException {
        System.out.println("Write the name of pokemon that want find");
        String name = scanner.nextLine();
        List<Pokemon> pokemons = PokemonRepository.findByName(name);
        for (int i = 0; i < pokemons.size(); i++) {
            System.out.printf("[%d] - [%d- %s -%s] -> [%d - %s]%n",
                    i,
                    pokemons.get(i).getId(),
                    pokemons.get(i).getName(),
                    pokemons.get(i).getType(),
                    pokemons.get(i).getTrainer().getId(),
                    pokemons.get(i).getTrainer().getName());
        }
    }

    private static void findAll() throws SQLException{
        List<Pokemon> pokemons = PokemonRepository.findAll();
        for (int i = 0; i < pokemons.size(); i++) {
            System.out.printf("[%d] - [%d- %s -%s] -> [%d - %s]%n",
                    i,
                    pokemons.get(i).getId(),
                    pokemons.get(i).getName(),
                    pokemons.get(i).getType(),
                    pokemons.get(i).getTrainer().getId(),
                    pokemons.get(i).getTrainer().getName());
        }
    }
}
