import dao.SysSettingsDao;
import model.SysSettings;

public class SettingsManager {

    private static SysSettingsDao sysSettingsDao = new SysSettingsDao();

    static int getTs() {
        return Integer.parseInt(sysSettingsDao.findById(1L).getValue());
    }

    static void setTs(int ts) {
        SysSettings oldTs = sysSettingsDao.findById(1L);
        oldTs.setValue(String.valueOf(ts));
        sysSettingsDao.update(oldTs);
    }
}
