package fudan.se.lab2.repository.impl;

import fudan.se.lab2.constant.FileConstant;
import fudan.se.lab2.constant.InfoConstant;
import fudan.se.lab2.entity.Cappuccino;
import fudan.se.lab2.repository.CappuccinoRepository;
import fudan.se.lab2.util.FileUtil;

import java.text.MessageFormat;

public class CappuccinoRepositoryImpl implements CappuccinoRepository {
    @Override
    public Cappuccino getCappuccino(String name) {
        return stringArrayToObject(FileUtil.readByName(name, FileConstant.CAPPUCCINO_CSV));
    }

    @Override
    public void createCappuccino(Cappuccino cappuccino) {
        FileUtil.write(objectToStringArray(cappuccino), FileConstant.CAPPUCCINO_CSV);
    }

    private String[] objectToStringArray(Cappuccino cappuccino) {
        // if user already exists, throw exception
        if (FileUtil.exist(cappuccino.getName(), FileConstant.CAPPUCCINO_CSV)) {
            throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_EXIST, "Coffee",
                    cappuccino.getName()));
        }
        String[] array = new String[4];
        array[0] = cappuccino.getName();
        array[1] = cappuccino.getDescription();
        array[2] = String.valueOf(cappuccino.getPrice());
        array[3] = String.valueOf(cappuccino.getSize());
        return array;
    }

    private Cappuccino stringArrayToObject(String[] array) {
        Cappuccino cappuccino = new Cappuccino();
        cappuccino.setName(array[0]);
        cappuccino.setDescription(array[1]);
        cappuccino.setPrice(Double.parseDouble(array[2]));
        cappuccino.setSize(Integer.parseInt(array[3]));
        return cappuccino;
    }

    public String getName() {
        return "Cappuccino";
    }
}
