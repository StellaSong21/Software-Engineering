package fudan.se.lab4.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import fudan.se.lab4.constant.FileConstant;
import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.service.LoggerService;
import fudan.se.lab4.service.impl.LoggerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;

public class FileUtil {
//    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
//    private static LoggerService loggerService = new LoggerServiceImpl();

    private static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            return true;
        } else {
            if (destFileName.endsWith(File.separator)) {
                throw new RuntimeException(MessageFormat.format(InfoConstant.FILE_NAME_INVAILD_FAILED_CREATED, destFileName));
            }
            // file exists?
            if (!file.getParentFile().exists()) {
                //if the parent dir is not exist, then create it.
//                LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(InfoConstant.PARENT_DIR_CREATING);
                if (!file.getParentFile().mkdirs()) {
                    throw new RuntimeException(InfoConstant.DIR_FAILED_CREATE);
                }
            }
            // create target file.
            try {
                if (file.createNewFile()) {
//                    LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(InfoConstant.FILE_SUCCEED_CREATE, destFileName);
                    return true;
                } else {
                    throw new RuntimeException(MessageFormat.format(InfoConstant.FILE_FAILED_CREATE, destFileName));
                }
            } catch (IOException e) {
//                LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(e.getMessage());
                throw new RuntimeException(MessageFormat.format(InfoConstant.FILE_FAILED_CREATE_REASON, destFileName,
                        e.getMessage()));
            }
        }
    }

    public static void write(String[] array, String dataFilePath) {
        if (createFile(dataFilePath)) {
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new FileWriter(dataFilePath, true));
                CsvWriter out = new CsvWriter(bw, FileConstant.CSV_SEPARATOR);
                out.writeRecord(array);
                out.flush();
                bw.flush();
                out.close();
                bw.close();
            } catch (IOException e) {
//                LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static String[] readByName(String Name, String dataFilePath) {
        CsvReader reader;
        try {
            reader = new CsvReader(dataFilePath, FileConstant.CSV_SEPARATOR,
                    Charset.forName(FileConstant.CHARSET));
            while (reader.readRecord()) {
                String[] item = reader.getValues();
                if (item[0].equals(Name)) {
                    return item;
                }
            }
        } catch (IOException e) {
//            LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

//        LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, Name));
        throw new RuntimeException(MessageFormat.format(InfoConstant.ENTITY_NOT_FOUND, Name));
    }

    public static boolean exist(String name, String dataFilePath) {
        createFile(dataFilePath);
        CsvReader reader;
        try {
            reader = new CsvReader(dataFilePath, FileConstant.CSV_SEPARATOR,
                    Charset.forName(FileConstant.CHARSET));
            while (reader.readRecord()) {
                String[] item = reader.getValues();
                if (item[0].equals(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
//            LoggerServiceImpl.getLoggerService().getLogger(InfoConstant.LOGGER_FAIL_STATUS).info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return false;
    }

}
