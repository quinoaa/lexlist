package space.quinoaa.lexlist.controller;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;
import space.quinoaa.lexlist.data.Dictionary;
import space.quinoaa.lexlist.data.Entry;
import space.quinoaa.lexlist.repository.Repositories;

import java.util.List;

public class DictController extends Controller {

    public DictController(Repositories repo) {
        super(repo);
    }

    @Override
    public void addEndpoints() {
        ApiBuilder.get("list", this::list);
        ApiBuilder.post("create", this::create);
        ApiBuilder.post("delete", this::delete);

        ApiBuilder.path("entry", ()->{
            ApiBuilder.post("list", this::listEntries);
            ApiBuilder.post("add", this::addEntry);
            ApiBuilder.post("remove", this::removeEntry);
            ApiBuilder.post("edit", this::editEntry);
            ApiBuilder.post("data", this::getEntryData);
        });
    }



    private void listEntries(@NotNull Context context) {
        var user = getUserOrError(context);
        var specs = context.bodyAsClass(ListEntriesSpecs.class);

        var dict = repo.dict.getDictionary(specs.dictid);
        if(dict == null || dict.ownerid() != user.id()) throw new UnauthorizedResponse();

        var ls = repo.dict.listEntryNames(dict.dictid());
        context.json(ls);
        context.status(HttpStatus.CREATED);
    }

    public record ListEntriesSpecs(long dictid) {}

    private void addEntry(Context ctx) {
        var user = getUserOrError(ctx);
        var specs = ctx.bodyAsClass(AddEntrySpecs.class);
        var dict = repo.dict.getDictionary(specs.dictid);
        if(dict == null || dict.ownerid() != user.id()) return;

        ctx.json(new AddEntryRes(repo.dict.addEntry(dict, specs.name)));
        ctx.status(HttpStatus.CREATED);
    }

    public record AddEntrySpecs(long dictid, String name) {}
    public record AddEntryRes(Entry created) {}

    private void removeEntry(Context ctx) {
        var user = getUserOrError(ctx);
        var specs = ctx.bodyAsClass(RemoveEntrySpecs.class);
        var dict = repo.dict.getDictionary(specs.dictid);
        if(dict == null || dict.ownerid() != user.id()) throw new NotFoundResponse();

        var entry = repo.dict.getEntry(specs.name, specs.dictid);
        if(entry == null) throw new NotFoundResponse();

        ctx.status(HttpStatus.CREATED);
        ctx.json(new RemoveEntryRes(repo.dict.removeEntry(entry)));
    }

    public record RemoveEntrySpecs(long dictid, String name) {}
    public record RemoveEntryRes(boolean success) {}

    private void editEntry(Context ctx) {
        var user = getUserOrError(ctx);
        var specs = ctx.bodyAsClass(EditEntrySpecs.class);
        var dict = repo.dict.getDictionary(specs.dictid);
        if(dict == null || dict.ownerid() != user.id()) throw new NotFoundResponse();

        var entry = repo.dict.getEntry(specs.name, specs.dictid);
        ctx.status(HttpStatus.CREATED);
        ctx.json(new EditEntryRes(repo.dict.editEntry(entry, specs.data) != null));
    }

    public record EditEntrySpecs(long dictid, String name, String data) {}
    public record EditEntryRes(boolean success) {}

    private void getEntryData(Context ctx) {
        var user = getUserOrError(ctx);
        var specs = ctx.bodyAsClass(GetEntrySpecs.class);
        var dict = repo.dict.getDictionary(specs.dictid);
        if(dict == null || dict.ownerid() != user.id()) throw new NotFoundResponse();

        var entry = repo.dict.getEntry(specs.name, specs.dictid);

        ctx.status(HttpStatus.CREATED);
        ctx.json(new GetEntryRes(entry));
    }

    public record GetEntrySpecs(long dictid, String name) {}
    public record GetEntryRes(Entry entry) {}







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
