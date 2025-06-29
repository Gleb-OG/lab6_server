package main.commands;

import main.exceptions.InvalidDataException;
import main.network.Request;
import main.utils.Validator;
import java.io.IOException;

/**
 * Команда, выводящая организации из коллекции, годовой оборот которых равен введенному.
 */
public class FilterByAnnualTurnover extends Command {

    public FilterByAnnualTurnover() {
        super("filter_by_annual_turnover <annual turnover>", "Вывод организаций, годовой оборот которых равен введенному", 1);
    }

    @Override
    public boolean check(Request request) {
        return request.getCommandArg().matches("^\\d+$");
    }

    @Override
    public String execute(Request request) throws InvalidDataException, IOException {
        try {
            String str = "";
            String sizeOfAnnualTurnover = request.getCommandArg();
            long annualTurnover = Validator.parseAnnualTurnover(sizeOfAnnualTurnover);

            int count = 0;
            for (int key : collectionManager.getCollection().keySet()) {
                if (collectionManager.getCollection().get(key).getAnnualTurnover() == annualTurnover) {
                    str += ("-------Organization-------" + "\nkey = " + key + "\n" +
                            collectionManager.getCollection().get(key));
                    count++;
                }
            }
            if (count == 0 || collectionManager.getCollection().values().isEmpty()) {
                if (count == 0 && !collectionManager.getCollection().values().isEmpty()) {
                    return ("В коллекции отсутствуют организации, годовой оборот которых равен " + sizeOfAnnualTurnover + ".");
                } else {
                    return ("Коллекция пуста.");
                }
            } else {
                return str + ("\nВсе организации, годовой оборот которых равен " + sizeOfAnnualTurnover + ".");
            }
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String execute(String[] args) throws InvalidDataException {
        String str = "";
        long annualTurnover = Long.parseLong(args[0]);
        int count = 0;

        for (int key : collectionManager.getCollection().keySet()) {
            if (collectionManager.getCollection().get(key).getAnnualTurnover() == annualTurnover) {
                str += ("-------Organization-------" + "\nkey = " + key + "\n" +
                        collectionManager.getCollection().get(key));
                count++;
            }
        }

        if (count == 0 || collectionManager.getCollection().values().isEmpty()) {
            if (count == 0 && !collectionManager.getCollection().values().isEmpty()) {
                return ("В коллекции отсутствуют организации, годовой оборот которых равен " + annualTurnover + ".");
            } else {
                return ("Коллекция пуста.");
            }
        } else {
            return str + ("\nВсе организации, годовой оборот которых равен " + annualTurnover + ".");
        }
    }
}