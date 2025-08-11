import { createSignal, For, Show, Switch } from "solid-js";
import type { User } from "../types/user";
import "./app.scss"
import type { Dictionary } from "../types/dictionary";
import { fetchCreateDict, fetchDictList } from "../api/dictionary";
import spinner from "../media/spinner.svg"
import add from "../media/add.svg"
import more from "../media/more.svg"


export function DictionaryList(){
    const [list, setList] = createSignal<Dictionary[]|undefined>()

    const [name, setName] = createSignal("")

    fetchDictList().then(dictionaries=>{
        setList(dictionaries);
    });

    const addDictionary = ()=>{
        if(name().trim().length == 0) return;

        fetchCreateDict(name().trim()).then(res=>{
            let ls = list()?.map(id=>id)
            ls?.push(res.created)
            setList(ls)
        })
        setName("")
    }

    return (
        <div class="dict-list">
            <Show when={list()} fallback={<img src={spinner}/>}>
                <div>
                    <input
                        placeholder="Create dictionary"
                        on:input={event=>setName(event.target.value)}
                        value={name()}
                    />
                    <button onclick={addDictionary}><img src={add}/></button>
                </div>
                <For each={list() as Dictionary[]}>
                    {dict=>(
                        <div>
                            <span>{dict.name}</span>
                            <img src={more}/>
                        </div>
                    )}
                </For>
            </Show>
        </div>
    )
}



export function App(props: {user: User}){
    

    return (
        <div class="page-app">
            <DictionaryList />
        </div>
    )
}