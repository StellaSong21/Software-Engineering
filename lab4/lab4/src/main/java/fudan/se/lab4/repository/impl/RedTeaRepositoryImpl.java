package fudan.se.lab4.repository.impl;

import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.entity.RedTea;
import fudan.se.lab4.repository.RedTeaRepository;
import fudan.se.lab4.util.FileUtil;

import java.text.MessageFormat;

public class RedTeaRepositoryImpl implements RedTeaRepository {
    @Override
    public RedTea getRedTea(String name) {
        return stringArrayToObject(FileUtil.readByName(name, FileConstant.REDTEA_CSV));
    }

    @Override
    public void createRedTea(RedTea redTea) {
        FileUtil.write(objectToStringArray(redTea), FileConstant.REDTEA_CSV);
    }

    private String[] objectToStringArray(RedTea redTea) {
        // if user already exists, throw exception
        if (FileUtil.exist(redTea.getName(), FileConstant.REDTEA_CSV)) {
            throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_EXIST, "Tea",
                    redTea.getName()));
        }
        String[] array = new String[4];
        array[0] = redTea.getName();
        array[1] = redTea.getDescription();
        array[2] = String.valueOf(redTea.getPrice());
        array[3] = String.valueOf(redTea.getSize());
        return array;
    }

    private RedTea stringArrayToObject(String[] array) {
        RedTea redTea = new RedTea();
        redTea.setName(array[0]);
        redTea.setDescription(array[1]);
        redTea.setPrice(Double.parseDouble(array[2]));
        redTea.setSize(Integer.parseInt(array[3]));
        return redTea;
    }

    public String getName(){
        return "RedTea";
    }
}
