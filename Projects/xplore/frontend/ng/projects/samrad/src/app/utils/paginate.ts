import { ProjektRsp as Projekt } from "../../../../../generated/samrad-api";
import { XpPage } from "../../../../lib/ui/paginated-table/page";

export function paginate(samrad: Projekt[], pageIndex: number, pageSize: number) {
  const startIndex = pageIndex * pageSize;
  const endIndex = (pageIndex + 1) * pageSize;
  const paginatedList = samrad.slice(startIndex, endIndex);

  const samradPage: XpPage<Projekt> = {
    content: paginatedList,
    number: pageIndex,
    numberOfElements: pageSize,
    totalElements: samrad.length,
    totalPages: pageSize / paginatedList.length,
  };
  return samradPage;
}
