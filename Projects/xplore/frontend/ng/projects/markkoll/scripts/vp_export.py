import openpyxl
import json
import sys

def export_json(path_in, path_out):
    workbook = openpyxl.load_workbook(path_in, data_only=True)
    worksheet = workbook.active
    vp = export_vp(worksheet)
    with open(path_out, 'w') as json_file:
        json.dump(vp, json_file, indent=2)

def export_vp(worksheet):
    return export_node(vp_cells(), worksheet)

def export_node(node, worksheet):
    if isinstance(node, dict):
        return { k: export_node(v, worksheet) for (k,v) in node.items() }
    else:
        return worksheet[node].value

def vp_cells():
  return {
      "skogsmark": {
          "prisomrade": {
              "norrlandsInland": "N42",
              "norrlandsKustland": "N43",
              "tillvaxtomrade3": "N44",
              "tillvaxtomrade4A": "N45",
              "tillvaxtomrade4B": "N46"
          }
      },

      "punktersattning": {
          "kabelSkap": {
              "skog": "N17",
              "jordbruksimpediment": "N18",
              "ovrigMark": "N19"
          },
          "natstation": {
              "skog": {
                  "yta6x6m": "N20",
                  "yta8x8m": "N21",
                  "yta10x10m": "N22"
              },
              "jordbruksimpediment": {
                  "yta6x6m": "N23",
                  "yta8x8m": "N24",
                  "yta10x10m": "N25"
              },
              "ovrigMark": {
                  "yta6x6m": "N26",
                  "yta8x8m": "N27",
                  "yta10x10m": "N28"
              }
          },
          "sjokabelskylt": {
              "skog": {
                  "yta6x6m": "N29",
                  "yta8x8m": "N30",
                  "yta10x10m": "N31"
              },
              "jordbruksimpediment": {
                  "yta6x6m": "N32",
                  "yta8x8m": "N33",
                  "yta10x10m": "N34"
              },
              "ovrigMark": {
                  "yta6x6m": "N35",
                  "yta8x8m": "N36",
                  "yta10x10m": "N37"
              }
          }
      },

      "markledning": {
          "factor": "L19"
      },

      "vaganlaggning": {
          "zon1": "L21",
          "zon2": "L23"
      },

      "prisbasbelopp": "L29",
      "minimumersattning": "L27",
      "forhojdMinimumersattning": "L33",
      "sarskildErsattningSkogsbruksavtalet": "L25"
}

if __name__ == '__main__':
  path_in = sys.argv[1];
  path_out = sys.argv[2];

  export_json(path_in, path_out)
