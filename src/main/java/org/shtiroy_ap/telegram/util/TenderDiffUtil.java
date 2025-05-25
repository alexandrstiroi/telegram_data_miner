package org.shtiroy_ap.telegram.util;

import org.shtiroy_ap.telegram.model.Lot;
import org.shtiroy_ap.telegram.model.TenderDetailDto;

import java.util.Objects;

public class TenderDiffUtil {

    public static String generateDiffSummry(TenderDetailDto oldJson, TenderDetailDto newJson){
        StringBuilder sb = new StringBuilder();
        if (oldJson.getAmount().compareTo(newJson.getAmount()) != 0) {
            sb.append("🔸 Поле изменено: Сумма")
                    .append("\nБыло: ").append(oldJson.getAmount())
                    .append("\nСтало: ").append(newJson.getAmount())
                    .append("\n\n");
        }
        if (!Objects.equals(oldJson.getDate(), newJson.getDate())) {
            sb.append("🔸 Поле изменено: Дата")
                    .append("\nБыло: ").append(oldJson.getDate())
                    .append("\nСтало: ").append(newJson.getDate())
                    .append("\n\n");
        }
        for (Lot oLot : oldJson.getLots()){
            for (Lot nLot : newJson.getLots()){
                if (oLot.getUuid().equals(nLot.getUuid())){
                    if (oLot.getLotSuppliers() != null && oLot.getLotSuppliers().size() != nLot.getLotSuppliers().size()){
                        sb.append("🔸 Поле изменено: Лот")
                                .append("\nБыло: ").append(oLot.getLotSuppliers())
                                .append("\nСтало: ").append(nLot.getLotSuppliers())
                                .append("\n\n");
                    }
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : "Без подробностей, изменения в тендере";
    }
}
