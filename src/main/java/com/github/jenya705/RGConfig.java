package com.github.jenya705;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RGConfig {

    private static final Yaml yaml = new Yaml();

    private int outRegionMaxX = 5;
    private int outRegionMaxY = 5;
    private int outRegionMaxZ = 5;
    private int inRegionMaxX = 1;
    private int inRegionMaxY = 1;
    private int inRegionMaxZ = 1;
    private int freeRegionSize = 100;
    private int fullyRegionCoefficient = 0;
    private int intersectRegionCoefficient = 4;
    private int outRegionCoefficient = 8;
    private int regionsOnPage = 5;
    private boolean printCauseOfPriceCountCommand = true;
    private boolean printCauseOfPriceCreateCommand = true;
    private boolean listPrintMemberRegions = false;
    private float removeReturnCoefficient = 1.5f;
    private int listRegionCount = 10;

    private String confirmBeforeCreate = "&cВы не находитесь в списке на " +
            "подтверждение покупки региона, пожалуйста напишите команда /rg create сначала";
    private String regionCreated = "&aРегион создан!";
    private String transactionError = "&cОшибка в транзакции!";
    private String balanceLessThanPrice = "&cУ вас недостаточный баланс!";
    private String strangerRegionInSelection = "&dВ вашем выделении есть чужой регион!";
    private String priceMessage = "Ваше выделение будет стоить &a{0} зелени&r\n";
    private String priceCauseFully = "&eДанное выделение полностью входит в ваш регион";
    private String priceCauseIntersect = "&eДанное выделение пересекается с вашими регионами";
    private String priceCauseOut = "&eДанное выделение не пересекается с регионами";
    private String confirmCreate = "Для подтверждения покупки региона &e{0}&r за &a{1} " +
            "зелени&r напишите /rg confirm\n";
    private String regionIntersectsNotFound = "&eПересечений с регионами не нашлось";
    private String regionIntersectsFound = "&eПересечения с регионами:";
    private String selectionNotExist = "&cВы не выбрали регион, который бы хотели купить!";
    private String regionWithNameAlreadyExists = "&cРегион с таким названием уже существует!";
    private String regionNameNotGiven = "&cВы не написали название региона!";
    private String regionMore = "И ещё {0} регионов...";
    private String regionNeedToBeMinimum = "Регион должен быть минимум &3{0}&r блоков";
    private String regionSizeFormat = "{0}x{1}x{2}";
    private String firstFreeRegion = "Первый регион {0}x{0} доступен для привата бесплатно";
    private String listRegion = "Список ваших регионов:";
    private String removeRegionNotExist = "Региона с таким именем не существует";
    private String removeRegionNotOwner = "Вы не можете удалить регион, где вы не владелец";
    private String removeRegionSuccess = "Вы успешно удалили регион &e{0}&r, Вам было возвращено: &a{1} зелени";

    @SuppressWarnings("unchecked")
    public RGConfig() throws IOException {

        File configFile = new File(RGPlugin.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()){
            Map<String, Object> map = new LinkedHashMap<>();
            save(map);
            configFile.createNewFile();
            Files.write(configFile.toPath(), yaml.dumpAsMap(map)
                    .getBytes(StandardCharsets.UTF_16), StandardOpenOption.WRITE);
        }
        String yamlString = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_16);
        load((Map<String, Object>) yaml.load(yamlString));

    }

    private void load(Map<String, Object> map){
        outRegionMaxX = (int) map.getOrDefault("outRegionMaxX", getOutRegionMaxX());
        outRegionMaxY = (int) map.getOrDefault("outRegionMaxY", getOutRegionMaxY());
        outRegionMaxZ = (int) map.getOrDefault("outRegionMaxZ", getOutRegionMaxZ());
        inRegionMaxX = (int) map.getOrDefault("inRegionMaxX", getInRegionMaxX());
        inRegionMaxY = (int) map.getOrDefault("inRegionMaxY", getInRegionMaxY());
        inRegionMaxZ = (int) map.getOrDefault("inRegionMaxZ", getInRegionMaxZ());
        freeRegionSize = (int) map.getOrDefault("freeRegionSize", getFreeRegionSize());
        fullyRegionCoefficient = (int) map.getOrDefault("fullyRegionCoefficient", getFullyRegionCoefficient());
        intersectRegionCoefficient = (int) map.getOrDefault("intersectRegionCoefficient", getIntersectRegionCoefficient());
        outRegionCoefficient = (int) map.getOrDefault("outRegionCoefficient", getOutRegionCoefficient());
        regionsOnPage = (int) map.getOrDefault("regionsOnPage", getRegionsOnPage());
        printCauseOfPriceCountCommand = (boolean) map.getOrDefault("printCauseOfPriceCountCommand", isPrintCauseOfPriceCountCommand());
        printCauseOfPriceCreateCommand = (boolean) map.getOrDefault("printCauseOfPriceCreateCommand", isPrintCauseOfPriceCountCommand());
        listPrintMemberRegions = (boolean) map.getOrDefault("listPrintMemberRegions", isListPrintMemberRegions());
        listRegionCount = (int) map.getOrDefault("listRegionCount", getListRegionCount());
        removeReturnCoefficient = (float) (double) map.getOrDefault("removeReturnCoefficient", getRemoveReturnCoefficient());

        confirmBeforeCreate = loadMessage(map, "confirmBeforeCreate", getConfirmBeforeCreate());
        regionCreated = loadMessage(map, "regionCreated", getRegionCreated());
        transactionError = loadMessage(map, "transactionError", getTransactionError());
        balanceLessThanPrice = loadMessage(map, "balanceLessThanPrice", getBalanceLessThanPrice());
        strangerRegionInSelection = loadMessage(map, "strangerRegionInSelection", getStrangerRegionInSelection());
        priceMessage = loadMessage(map, "priceMessage", getPriceMessage());
        priceCauseFully = loadMessage(map, "priceCauseFully", getPriceCauseFully());
        priceCauseIntersect = loadMessage(map, "priceCauseIntersect", getPriceCauseIntersect());
        priceCauseOut = loadMessage(map, "priceCauseOut", getPriceCauseOut());
        confirmCreate = loadMessage(map, "confirmCreate", getConfirmCreate());
        regionIntersectsNotFound = loadMessage(map, "regionIntersectsNotFound", getRegionIntersectsNotFound());
        regionIntersectsFound = loadMessage(map, "regionIntersectsFound", getRegionIntersectsFound());
        selectionNotExist = loadMessage(map, "selectionNotExist", getSelectionNotExist());
        regionWithNameAlreadyExists = loadMessage(map, "regionWithNameAlreadyExists", getRegionWithNameAlreadyExists());
        regionNameNotGiven = loadMessage(map, "regionNameNotGiven", getRegionNameNotGiven());
        regionMore = loadMessage(map, "regionMore", getRegionMore());
        regionNeedToBeMinimum = loadMessage(map, "regionNeedToBeMinimum", getRegionNeedToBeMinimum());
        regionSizeFormat = loadMessage(map, "regionSizeFormat", getRegionSizeFormat());
        firstFreeRegion = loadMessage(map, "firstFreeRegion", getFirstFreeRegion());
        listRegion = loadMessage(map, "listRegion", getListRegion());
        removeRegionNotExist = loadMessage(map, "removeRegionNotExist", getRemoveRegionNotExist());
        removeRegionNotOwner = loadMessage(map, "removeRegionNotOwner", getRemoveRegionNotOwner());
        removeRegionSuccess = loadMessage(map, "removeRegionSuccess", getRemoveRegionSuccess());

    }

    @SuppressWarnings("unchecked")
    private String loadMessage(Map<String, Object> from, String key, String defaultValue){
        Map<String, Object> messagesMap = (Map<String, Object>) from.getOrDefault("messages", null);
        if (messagesMap == null) {
            messagesMap = new LinkedHashMap<>();
            from.put("messages", messagesMap);
        }
        return ((String) messagesMap.getOrDefault(key, defaultValue)).replaceAll("&",
                Character.toString(ChatColor.COLOR_CHAR));
    }

    private void save(Map<String, Object> map){
        map.put("outRegionMaxX", getOutRegionMaxX());
        map.put("outRegionMaxY", getOutRegionMaxY());
        map.put("outRegionMaxZ", getOutRegionMaxZ());
        map.put("inRegionMaxX", getInRegionMaxX());
        map.put("inRegionMaxY", getInRegionMaxY());
        map.put("inRegionMaxZ", getInRegionMaxZ());
        map.put("freeRegionSize", getFreeRegionSize());
        map.put("fullyRegionCoefficient", getFullyRegionCoefficient());
        map.put("intersectRegionCoefficient", getIntersectRegionCoefficient());
        map.put("outRegionCoefficient", getOutRegionCoefficient());
        map.put("regionsOnPage", getRegionsOnPage());
        map.put("printCauseOfPriceCountCommand", isPrintCauseOfPriceCountCommand());
        map.put("printCauseOfPriceCreateCommand", isPrintCauseOfPriceCreateCommand());
        map.put("listPrintMemberRegions", isListPrintMemberRegions());
        map.put("listRegionCount", getListRegionCount());
        map.put("removeReturnCoefficient", getRemoveReturnCoefficient());

        saveMessage(map, "confirmBeforeCreate", getConfirmBeforeCreate());
        saveMessage(map, "regionCreated", getRegionCreated());
        saveMessage(map, "transactionError", getTransactionError());
        saveMessage(map, "balanceLessThanPrice", getBalanceLessThanPrice());
        saveMessage(map, "strangerRegionInSelection", getStrangerRegionInSelection());
        saveMessage(map, "priceMessage", getPriceMessage());
        saveMessage(map, "priceCauseFully", getPriceCauseFully());
        saveMessage(map, "priceCauseIntersect", getPriceCauseIntersect());
        saveMessage(map, "priceCauseOut", getPriceCauseOut());
        saveMessage(map, "confirmCreate", getConfirmCreate());
        saveMessage(map, "regionIntersectsNotFound", getRegionIntersectsNotFound());
        saveMessage(map, "regionIntersectsFound", getRegionIntersectsFound());
        saveMessage(map, "selectionNotExist", getSelectionNotExist());
        saveMessage(map, "regionNameWithAlreadyExists", getRegionWithNameAlreadyExists());
        saveMessage(map, "regionNameNotGiven", getRegionNameNotGiven());
        saveMessage(map, "regionMore", getRegionMore());
        saveMessage(map, "regionNeedToBeMinimum", getRegionNeedToBeMinimum());
        saveMessage(map, "regionSizeFormat", getRegionSizeFormat());
        saveMessage(map, "firstFreeRegion", getFirstFreeRegion());
        saveMessage(map, "listRegion", getListRegion());
        saveMessage(map, "removeRegionNotExist", getRemoveRegionNotExist());
        saveMessage(map, "removeRegionNotOwner", getRemoveRegionNotOwner());
        saveMessage(map, "removeRegionSuccess", getRemoveRegionSuccess());

    }

    @SuppressWarnings("unchecked")
    private void saveMessage(Map<String, Object> map, String key, String value){
        Map<String, Object> messages = (Map<String, Object>) map.getOrDefault("messages", null);
        if (messages == null) {
            messages = new LinkedHashMap<>();
            map.put("messages", messages);
        }
        messages.put(key, value);
    }

}
