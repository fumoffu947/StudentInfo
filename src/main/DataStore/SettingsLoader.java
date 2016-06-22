package main.DataStore;

import main.MainFrame;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by phili on 2016-02-18.
 * every onption read in is terminated by an ";" so string split
 * will be easy and to easily parion the sring by option
 * lastnum:num,num,num,num;
 */
public class SettingsLoader {

    private Logger logger = MainFrame.logger;

    private List<Integer> scatteredIDNumbers = new ArrayList<>();
    private int lastIDNumber;

    public SettingsLoader(BufferedInputStream fileReader) {
        try {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int character = fileReader.read();
            while (character != -1) {
                byteBuffer.write(character);
                character = fileReader.read();
            }


            if (byteBuffer.size() > 1) {
                String settingsInfo = new String(Base64.getDecoder().decode(byteBuffer.toByteArray()));
                String[] settingsArray = settingsInfo.split(";");
                String[] idArrayInfo = settingsArray[0].split(":");

                lastIDNumber = Integer.parseInt(idArrayInfo[0]);

                if (idArrayInfo.length != 1) {
                    String[] scatteredIDArray = idArrayInfo[1].split(",");
                    for (int scatteredIndex = 0; scatteredIndex < scatteredIDArray.length; scatteredIndex++) {
                        scatteredIDNumbers.add(Integer.parseInt(scatteredIDArray[scatteredIndex]));
                    }
                }
            }else {
                scatteredIDNumbers = new ArrayList<>();
                lastIDNumber = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.log(Level.CONFIG,"Loaded lastID as "+lastIDNumber+" and scattered as "+scatteredIDNumbers);
    }

    public void saveSettingsInfo(BufferedOutputStream fileWriter) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(lastIDNumber);
        stringBuilder.append(":");

        for (int index = 0; index < scatteredIDNumbers.size(); index++) {
            stringBuilder.append(scatteredIDNumbers.get(index));
            if (index != scatteredIDNumbers.size()-1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(";");

        try {
            fileWriter.write(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getAScatterdIDNumber() {
        if (!scatteredIDNumbers.isEmpty()) {
            return scatteredIDNumbers.remove(0);
        }
        return -1;
    }

    public List<Integer> getScatteredIDNumbers() {
        return scatteredIDNumbers;
    }

    public int getLastIDNumber() {
        return lastIDNumber;
    }

    public void setLastIDNumber(final int lastIDNumber) {
        this.lastIDNumber = lastIDNumber;
    }

    public void addScatterdIdNumber(final int scatteredIDNumber) {
        logger.log(Level.CONFIG,"Added scattered number: "+scatteredIDNumber);
        scatteredIDNumbers.add(scatteredIDNumber);
        Collections.sort(scatteredIDNumbers);
    }
}
