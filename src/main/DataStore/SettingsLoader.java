package main.DataStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by phili on 2016-02-18.
 * every onption read in is terminated by an ";" so string split
 * will be easy and to easily parion the sring by option
 * lastnum:num,num,num,num;
 */
public class SettingsLoader {

    private List<Integer> scatteredIDNumbers = new ArrayList<>();
    private int lastIDNumber = 0;

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

                String[] scatterdIDArray = idArrayInfo[1].split(",");
                for (int scatterdIndex = 0; scatterdIndex < scatterdIDArray.length; scatterdIndex++) {
                    scatteredIDNumbers.add(Integer.parseInt(scatterdIDArray[scatterdIndex]));
                }
            }else {
                scatteredIDNumbers = new ArrayList<>();
                lastIDNumber = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void addScatterdIdNumber(final int scatterdIdNumber) {
        scatteredIDNumbers.add(scatterdIdNumber);
        Collections.sort(scatteredIDNumbers);
    }
}
