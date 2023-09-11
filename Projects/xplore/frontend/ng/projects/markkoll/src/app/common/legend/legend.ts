export interface Legend {
  groups: LegendGroup[];
}

export interface LegendGroup {
  title: string;
  items: LegendItem[];
}

export interface LegendItem {
  title: string;
  iconUrl: string;
  iconName?: string;
}
