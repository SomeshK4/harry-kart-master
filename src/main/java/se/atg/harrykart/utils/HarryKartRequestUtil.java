package se.atg.harrykart.utils;

public class HarryKartRequestUtil extends BaseUtil {

    private static HarryKartRequestUtil harryKartRequestUtil;

    private HarryKartRequestUtil() {
        super("se.atg.harrykart.schemas", "input.xsd");
    }

    public static HarryKartRequestUtil instance() {
        if (harryKartRequestUtil == null) {
            harryKartRequestUtil = new HarryKartRequestUtil();
        }
        return harryKartRequestUtil;
    }
}
