package main.commands;

import main.Server;
import main.model.*;
import main.exceptions.InvalidDataException;
import main.network.Request;
import main.utils.CSVProcessor;
import main.utils.IDGenerator;
import main.utils.Validator;
import java.util.TreeMap;

/**
 * Команда, обновляющая по id организацию из коллекции.
 */
public class UpdateID extends Command {

    public UpdateID() {
        super("update <id>", "Обновление значения элемента коллекции по его id.", 1);
    }

    @Override
    public int getArgsAmount() {
        return Server.scriptMode ? 2 : 1;
    }

    @Override
    public boolean check(Request request) {
        if (!request.getCommandArg().matches("^\\d+$")) return false;
        int id = Integer.parseInt(request.getCommandArg());
        return IDGenerator.checkIdExisting(id);
    }

    @Override
    public String execute(Request request) {
        try {
            String updatingID = request.getCommandArg();
            if (!updatingID.matches("^\\d+$")) {
                throw new InvalidDataException("id может быть только натуральным числом.");
            }

            int id = Validator.validateInt(updatingID);
            TreeMap<Integer, Organization> collection = collectionManager.getCollection();
            int key = 0;
            for (int k : collection.keySet()) if (collection.get(k).getID() == id) key = k;

            if (key == 0) {
                return ("В коллекции отсутствует элемент с id " + id + ".");
            }

            collectionManager.updateKey(key, (Organization) request.getCommandObjArg());
            return ("Элемент c id " + id + " успешно обновлен.");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String execute(String[] args) throws InvalidDataException {
        int id = Integer.parseInt(args[0]);
        TreeMap<Integer, Organization> collection = collectionManager.getCollection();

        int key = 0;
        for (int k : collection.keySet()) if (collection.get(k).getID() == id) key = k;
        if (key == 0) {
            return ("В коллекции отсутствует элемент с id " + id + ".");
        }

        collectionManager.removeOrganizationByKey(key);
        Organization newOrganization = CSVProcessor.parseOrganizationFromString(args[1]);
        collectionManager.addOrganization(key, newOrganization);
        return ("Элемент c id " + id + " успешно обновлен.");
    }
}
