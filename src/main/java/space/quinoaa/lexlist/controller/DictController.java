package space.quinoaa.lexlist.controller;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import space.quinoaa.lexlist.data.Dictionary;
import space.quinoaa.lexlist.repository.Repositories;

public class DictController extends Controller {

    public DictController(Repositories repo) {
        super(repo);
    }

    @Override
    public void addEndpoints() {
        ApiBuilder.get("list", this::list);
        ApiBuilder.post("create", this::create);
        ApiBuilder.post("delete", this::delete);
    }

    private void list(@NotNull Context context) {
        var user = getUserOrError(context);

        context.json(repo.dict.listDictionaries(user));
    }

    private void create(@NotNull Context context) {
        var user = getUserOrError(context);
        var specs = context.bodyAsClass(CreateSpecs.class);

        var dict = repo.dict.createDictionary(user, specs.name);
        context.json(new CreateResult(dict));
        context.status(HttpStatus.CREATED);
    }

    public record CreateSpecs(String name) {}

    public record CreateResult(Dictionary created) {}


    private void delete(@NotNull Context context) {
        var user = getUserOrError(context);
        var specs = context.bodyAsClass(DeleteSpecs.class);
        var dict = repo.dict.getDictionary(specs.id);
        if(dict == null || dict.ownerid() != user.id()){
            context.json(new DeleteResult(false));
        }else{
            context.json(new DeleteResult(repo.dict.deleteDictionary(dict)));
        }
        context.status(HttpStatus.CREATED);
    }

    public record DeleteSpecs(long id) {}

    public record DeleteResult(boolean success) {}



}
