package SemanticAnalysis.Table;

import java.util.ArrayList;

public class SymbolTable {
    public String m_name= null;
    public ArrayList<SymbolTableEntry> m_symlist = null;
    public int m_size= 0;
    public int m_tablelevel = 0;
    public SymbolTable m_uppertable = null;


    public SymbolTable(int p_level, SymbolTable p_uppertable){
        m_tablelevel = p_level;
        m_name = null;
        m_symlist = new ArrayList<>();
        m_uppertable = p_uppertable;
    }

    public SymbolTable(int p_level, String p_name, SymbolTable p_uppertable){
        m_tablelevel = p_level;
        m_name = p_name;
        m_symlist = new ArrayList<SymbolTableEntry>();
        m_uppertable = p_uppertable;
    }

    public void addEntry(SymbolTableEntry p_entry){
        m_symlist.add(p_entry);
    }

    public SymbolTableEntry lookupName(String p_tolookup) {
        SymbolTableEntry returnvalue = new SymbolTableEntry();
        boolean found = false;
        for( SymbolTableEntry rec : m_symlist) {
            if (p_tolookup.equals(rec.m_name)) {
                returnvalue = rec;
                found = true;
            }
        }
        if (!found) {
            if (m_uppertable != null) {
                returnvalue = m_uppertable.lookupName(p_tolookup);
            }
        }
        return returnvalue;
    }

    public String toString(){
        StringBuilder stringtoreturn = new StringBuilder();
        String prelinespacing = "";
        for (int i = 0; i < this.m_tablelevel; i++)
            prelinespacing += "|    ";
        stringtoreturn.append("\n").append(prelinespacing).append("=====================================================\n");
        stringtoreturn.append(prelinespacing).append(String.format("%-25s", "| table: " + m_name)).append("\n");//append(String.format("%-27s", " scope offset: " + m_size)).append("|\n");
        stringtoreturn.append(prelinespacing).append("=====================================================\n");
        for (SymbolTableEntry SymbolTableEntry : m_symlist) {
            stringtoreturn.append(prelinespacing).append(SymbolTableEntry.toString()).append('\n');
        }
        stringtoreturn.append(prelinespacing).append("=====================================================");
        return stringtoreturn.toString();
    }
}
