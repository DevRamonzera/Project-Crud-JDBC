package com.br.project_crud_jdbc.service;

import com.br.project_crud_jdbc.domain.Trainer;
import com.br.project_crud_jdbc.repository.TrainerRepository;
import com.br.project_crud_jdbc.test.Test;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Log4j2
public class TrainerService {
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

    private static void menuMain(){
        Test.menuMain();
    }

    private static void save() throws SQLException{
        System.out.println("Write the name of trainer that want save");
        String name = scanner.nextLine();
        Trainer trainer = Trainer.builder().name(name).build();
        TrainerRepository.save(trainer);
    }

    private static void deleted() throws SQLException{
        System.out.println("Write the id of trainer that want delete");
        Integer id = Integer.parseInt(scanner.nextLine());
        System.out.println("Are you sure? S/N");
        String choice = scanner.nextLine();
        if("s".equalsIgnoreCase(choice)){
            TrainerRepository.delete(id);
        }
        if("n".equalsIgnoreCase(choice)){
            log.info("Action was cancelled");
        }
    }

    private static void update() throws SQLException{
        System.out.println("Write the id of the trainer that want to update");
        Optional<Trainer> trainerOptional = TrainerRepository.findById(Integer.parseInt(scanner.nextLine()));
        if(trainerOptional.isEmpty()){
            log.info("Trainer not found");
            return;
        }
        Trainer trainerFromDatabase = trainerOptional.get();
        System.out.println("Write the new name or enter to keep the same");
        String name = scanner.nextLine();
        name = name.isEmpty() ? trainerFromDatabase.getName() : name;
        Trainer trainerUpdate = Trainer.builder()
                .id(trainerFromDatabase.getId())
                .name(name)
                .build();
        TrainerRepository.update(trainerUpdate);
    }

    private static void findByName() throws SQLException {
        System.out.println("Write the name of trainer that want find");
        String name = scanner.nextLine();
        List<Trainer> trainers = TrainerRepository.findByName(name);
        for (int i = 0; i < trainers.size(); i++) {
            System.out.printf("[%d] - id: %d - %s%n", i, trainers.get(i).getId(),trainers.get(i).getName());
        }
    }

    private static void findAll() throws SQLException{
        List<Trainer> allTrainers = TrainerRepository.findAll();
        for (int i = 0; i < allTrainers.size(); i++) {
            System.out.printf("[%d] - id: %d - %s%n", i, allTrainers.get(i).getId(), allTrainers.get(i).getName());
        }
    }
}
