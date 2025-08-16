package space.quinoaa.lexlist.repository;

import space.quinoaa.lexlist.data.Dictionary;
import space.quinoaa.lexlist.data.Entry;
import space.quinoaa.lexlist.data.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DictionaryRepository extends JDBCRepository {

    public DictionaryRepository(Connection connection) {
        super(connection);
    }

    public Dictionary createDictionary(User owner, String name){
        return query("create_dict", res->{
            if(!res.next()) return null;
            return new Dictionary(res.getLong("dictid"), owner.id(), name);
        }, arg->{
            arg.setLong(1, owner.id());
            arg.setString(2, name);
        });
    }

    public boolean deleteDictionary(Dictionary dict){
        return query(
                "delete_dict",
                ResultSet::next,
                arg->arg.setLong(1, dict.dictid())
        );
    }

    public Dictionary renameDictionary(Dictionary dict, String name){
        return query("rename_dict", res->{
            if(res.next()) return dict.withName(name);
            return dict;
        }, args->{
            args.setString(1, name);
            args.setLong(2, dict.dictid());
        });
    }

    public List<Dictionary> listDictionaries(User user){
        return query("list_user_dict", res->{
            var list = new ArrayList<Dictionary>();

            while(res.next()){
                list.add(new Dictionary(
                        res.getLong("dictid"),
                        res.getLong("ownerid"),
                        res.getString("name")
                ));
            }

            return list;
        }, arg->{
            arg.setLong(1, user.id());
        });
    }

    public Dictionary getDictionary(long id){
        return query("get_dict", res->{
            if(!res.next()) return null;
            return new Dictionary(
                    res.getLong("dictid"),
                    res.getLong("ownerid"),
                    res.getString("name")
            );
        }, arg->arg.setLong(1, id));
    }



    public List<String> listEntryNames(long dictid){
        return query("list_dict_entry_name", res->{
            var list = new ArrayList<String>();
            while(res.next()) list.add(res.getString("name"));
            return list;
        }, arg->arg.setLong(1, dictid));
    }

    public Entry addEntry(Dictionary dict, String name) {
        return query("add_entry", res->{
            if(!res.next()) return null;
            return new Entry(name, dict.dictid(), "");
        }, arg->{
            arg.setString(1, name);
            arg.setLong(2, dict.dictid());
            arg.setString(3, "");
        });
    }

    public Entry editEntry(Entry entry, String data) {
        return query("update_entry", res->{
            if(!res.next()) return null;
            return entry.withData(data);
        }, arg->{
            arg.setString(1, data);
            arg.setString(2, entry.name());
            arg.setLong(3, entry.dictid());
        });
    }

    public boolean removeEntry(Entry entry) {
        return query("delete_entry", ResultSet::next, arg->{
            arg.setString(1, entry.name());
            arg.setLong(2, entry.dictid());
        });
    }

    public Entry getEntry(String name, long dictid){
        return query("get_entry_data", res->{
            if(!res.next()) return null;
            return new Entry(
                    name,
                    dictid,
                    res.getString("data")
            );
        }, arg->{
            arg.setString(1, name);
            arg.setLong(2, dictid);
        });
    }

}
