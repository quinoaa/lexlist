import { createSignal, For, Match, Show, Switch } from "solid-js";
import type { User } from "../types/user";
import "./app.scss"
import type { Dictionary, Entry } from "../types/dictionary";
import { fetchAddEntry, fetchEntryData, fetchListEntries } from "../api/dictionary";
import { LoadingView } from "../components/loading";
import add from "../media/add.svg"
import { DictionaryList, DictionaryView } from "../components/dictionary";

export function DictionaryViewOld(prop: {dict: Dictionary}) {
    const [loaded, setLoaded] = createSignal(false)
    const [entries, setEntries] = createSignal<string[]>([])

    const [search, setSearch] = createSignal("")
    const [entry, setEntry] = createSignal<Entry|undefined>();

    fetchListEntries(prop.dict).then(resp=>{
        setEntries(resp.sort())
        
        setLoaded(true)
    })


    const addEntry = ()=>{
        if(!loaded()) return;
        let name = search()
        setSearch("");
        if(name.length === 0 || name in entries()) return;

        setLoaded(false);
        fetchAddEntry(prop.dict, name).then(entry=>{
            if(entry.created === undefined) return;
            setEntry(entry.created);
            
            let list = entries().map(id=>id);
            list.push(name);
            list.sort();
            setEntries(list)
            setLoaded(true);
        })
    }

    const openEntry = (name: string)=>{
        setLoaded(false);

        fetchEntryData(prop.dict, name).then(entry=>{
            if(entry.entry === undefined) throw Error("Entry is not found")

            setEntry(entry.entry)
            setLoaded(true)
        })
    }
    


    
    
    return (
        <div class="view-dict">
            <div class="content">
                <Switch>
                    <Match when={!loaded()}>
                        <LoadingView />
                    </Match>
                    <Match when={entry()}>
                        <div class="entryview">
                            <h1>{entry()?.name}</h1>
                            <p>{entry()?.data}</p>
                        </div>
                    </Match>
                    <Match when={true}>
                        <div class="wordlist">
                            <For
                                each={entries().filter(s=>s.indexOf(search()) != -1)}
                                fallback={<>Empty dictionary</>}
                            >
                                {entry=>
                                <div onClick={()=>openEntry(entry)}>
                                    {entry}
                                </div>
                                }
                            </For>
                        </div>
                        <div class="header">
                            <div>
                                <input
                                    placeholder="Search"
                                    value={search()}
                                    onInput={event=>setSearch(event.target.value)}
                                />
                                <img class="button" src={add} onClick={addEntry} />
                            </div>
                        </div>
                    </Match>
                </Switch>
            </div>
        </div>
    )
}


export function App(props: {user: User}){
    const [dict, setDict] = createSignal<Dictionary|undefined>()

    return (
        <div class="page-app">
            <Show
                when={dict() !== undefined}
                fallback={<DictionaryList onSelect={setDict} />}
            >
                <DictionaryView
                    dictionary={dict() as Dictionary}
                    onBack={()=>setDict()}
                />
            </Show>
        </div>
    )
}