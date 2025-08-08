package space.quinoaa.lexlist.repository;

import space.quinoaa.lexlist.data.Dictionary;
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






}
