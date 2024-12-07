import { reactive, onMounted } from "vue";
import { lovValueByLovCode} from "@/api/system/dict";

export function useKoiDict(dictType: Array<string>) {
  let koiDicts: any = reactive({});
  onMounted(async () => {
    if (dictType.length > 0) {
      for (const type of dictType) {
        const res: any = await lovValueByLovCode(type);
        if (res.data != null) {
          koiDicts[type] = res.data;
        }
      }
    }
  });
  return { koiDicts };
}
