package com.br.project_crud_jdbc.test;

import com.br.project_crud_jdbc.service.PokemonService;
import com.br.project_crud_jdbc.service.TrainerService;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.util.Scanner;

@Log4j2
public class Test {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        int op;
        while(true){
            menuMain();
            op = Integer.parseInt(scanner.nextLine());
            if(op == 0){
                log.info("Out system.");
                break;
            }
            switch(op){
                case 1 -> {
                    trainerMenu();
                    op = Integer.parseInt(scanner.nextLine());
                    TrainerService.menu(op);
                }
                case 2 -> {
                    pokemonMenu();
                    op = Integer.parseInt(scanner.nextLine());
                    PokemonService.menu(op);
                }
            }
        }
    }
    public static void menuMain(){
        System.out.println("Type number of your operation");
        System.out.println("1. Go for trainer");
        System.out.println("2. Go for pokemon");
        System.out.println("0. Exit");
    }

    private static void trainerMenu(){
        System.out.println("Type number of your operation");
        System.out.println("1. Save a trainer");
        System.out.println("2. Search trainer by name");
        System.out.println("3. Show all trainer");
        System.out.println("4. Update a trainer");
        System.out.println("5. delete a trainer");
        System.out.println("0. Back for manu main");
    }
    private static void pokemonMenu(){
        System.out.println("Type number of your operation");
        System.out.println("1. Save a pokemon");
        System.out.println("2. Search pokemon by name");
        System.out.println("3. Show all pokemon");
        System.out.println("4. Update a pokemon");
        System.out.println("5. delete a pokemon");
        System.out.println("0. Back for manu main");
    }
}
