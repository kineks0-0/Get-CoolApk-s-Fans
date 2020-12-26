import api.CoolApkFansApi;
import api.dataclass.Data;
import io.FileWrite;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) {
        //System.out.println(CoolApkFansApi.getRawCoolApkFansJson(2086596));
        //CoolApkFansApi.getFansData(2086596);
        //System.out.println(CoolApkFansApi.getFansName(2086596));
        ArrayList<String> fansData = CoolApkFansApi.getFansName(2086596);
        StringBuilder FansName = new StringBuilder();

        for (String str:fansData) {
            FansName.append(str).append("\n");
        }

        FileWrite.write2File("./FansNameList.txt",FansName.toString());
        System.out.println(FansName);
    }
}
