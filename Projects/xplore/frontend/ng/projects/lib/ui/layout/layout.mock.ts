import { MenuItem } from "./layout.component";

export const mockMenuItems: MenuItem[] = [
  {
    title: "Sida 1",
    selected: true,
    icon: "work_outline",
    items: [{
      title: "Undersida1-1",
      selected: false,
      path: "/undersida1-1",
      icon: "format_list_bulleted",
    },
    {
      title: "Undersida1-2",
      selected: false,
      path: "/undersida1-2",
      icon: "format_list_bulleted",
    }]
  },
  {
    title: "Sida 2",
    selected: false,
    icon: "work_outline",
    items: [{
      title: "Undersida2-1",
      selected: false,
      path: "/undersida2-1",
      icon: "format_list_bulleted",
    }]
  },
  {
    title: "Sida 3",
    selected: false,
    path: "/sida3",
    icon: "emoji_objects",
  },
  {
    title: "Sida 4",
    selected: false,
    path: "/sida4",
    icon: "emoji_objects",
    items: [],
  }];
