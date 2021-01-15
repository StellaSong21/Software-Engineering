package fudan.se.lab4.repository.impl;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.entity.GreenTea;
import fudan.se.lab4.repository.GreenTeaRepository;
import fudan.se.lab4.util.FileUtil;

import java.text.MessageFormat;

public class GreenTeaRepositoryImpl implements GreenTeaRepository {
    @Override
    public GreenTea getGreenTea(String name) {
        return stringArrayToObject(FileUtil.readByName(name, FileConstant.GREENTEA_CSV));
    }

    @Override
    public void createGreenTea(GreenTea greenTea) {
        FileUtil.write(objectToStringArray(greenTea), FileConstant.GREENTEA_CSV);
    }

    private String[] objectToStringArray(GreenTea greenTea) {
        // if user already exists, throw exception
        if (FileUtil.exist(greenTea.getName(), FileConstant.GREENTEA_CSV)) {
            throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_EXIST, "Tea",
                    greenTea.getName()));
        }
        String[] array = new String[4];
        array[0] = greenTea.getName();
        array[1] = greenTea.getDescription();
        array[2] = String.valueOf(greenTea.getPrice());
        array[3] = String.valueOf(greenTea.getSize());
        return array;
    }

    private GreenTea stringArrayToObject(String[] array) {
        GreenTea greenTea = new GreenTea();
        greenTea.setName(array[0]);
        greenTea.setDescription(array[1]);
        greenTea.setPrice(Double.parseDouble(array[2]));
        greenTea.setSize(Integer.parseInt(array[3]));
        return greenTea;
    }

    public String getName(){
        return "GreenTea";
    }
}
