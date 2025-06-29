package main.commands;

import main.exceptions.InvalidDataException;
import main.network.Request;
import main.utils.Validator;
import java.io.IOException;

/**
 * Команда, выводящая организации, название улицы которых по длине больше введенного.
 */
public class FilterGreaterThanOfficialAddress extends Command {

    public FilterGreaterThanOfficialAddress() {
        super("filter_greater_than_official_address <street name>", "Вывод организаций, официальный адрес (название улицы) которых по длине больше введенного", 1);
    }

    @Override
    public boolean check(Request request) {
        return !request.getCommandArg().isEmpty() &&
                (request.getCommandArg().length() <= 103);
    }

    @Override
    public String execute(Request request) throws InvalidDataException, IOException {

        if (collectionManager.getCollection().values().isEmpty()) {
            return ("Коллекция пуста.");
        }

        try {
            String streetName = Validator.validateStreetName(request.getCommandArg());
            String str = "";

            int count = 0;
            for (int key : collectionManager.getCollection().keySet()) {
                if (collectionManager.getCollection().get(key).getOfficialAddress() != null
                        && collectionManager.getCollection().get(key).getOfficialAddress()
                        .getStreet().length() > streetName.length()) {
                    str += ("-------Organization-------" + "\nkey = " + key + "\n" +
                            collectionManager.getCollection().get(key));
                    count++;
                }
            }

            if (count == 0 || collectionManager.getCollection().values().isEmpty()) {
                if (count == 0 && !collectionManager.getCollection().values().isEmpty()) {
                    return ("В коллекции отсутствуют организации, длина адреса которых больше " + streetName.length() + ".");
                } else {
                    return ("Коллекция пуста.");
                }
            } else {
                return str + ("\nВсе организации, длина адреса которых больше " + streetName.length() + ".");
            }
        } catch (InvalidDataException e) {
            return (e.getMessage());
        }
    }

    @Override
    public String execute(String[] args) throws InvalidDataException {
        String streetName = args[0];
        String str = "";

        int count = 0;
        for (int key : collectionManager.getCollection().keySet()) {
            if (collectionManager.getCollection().get(key).getOfficialAddress() != null
                    && collectionManager.getCollection().get(key).getOfficialAddress()
                    .getStreet().length() > streetName.length()) {
                str += ("-------Organization-------" + "\nkey = " + key + "\n" +
                        collectionManager.getCollection().get(key));
                count++;
            }
        }
        if (count == 0 || collectionManager.getCollection().values().isEmpty()) {
            if (count == 0 && !collectionManager.getCollection().values().isEmpty()) {
                return ("В коллекции отсутствуют организации, длина адреса которых больше " + streetName.length() + ".");
            } else {
                return ("Коллекция пуста.");
            }
        } else {
            return str + ("\nВсе организации, длина адреса которых больше " + streetName.length() + ".");
        }
    }
}