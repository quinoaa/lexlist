import "./dictionary.scss"

import { createSignal, For, Match, Show, Switch } from "solid-js";
import type { Dictionary, Entry } from "../types/dictionary";
import { fetchAddEntry, fetchCreateDict, fetchDictList, fetchEditEntry, fetchEntryData, fetchListEntries } from "../api/dictionary";
import { LoadingView } from "./loading";

import add from "../media/add.svg"
import back from "../media/back.svg"
import edit from "../media/edit.svg"
import check from "../media/check.svg"
import { BorderPage, ItemList } from "./interface";
import { TextEdit, TextView } from "./text";
import type Quill from "quill";

export function DictionaryList(prop: {onSelect: (dict: Dictionary)=>void}){
    const [list, setList] = createSignal<Dictionary[]|undefined>()
    let input: HTMLInputElement|undefined

    fetchDictList().then(dictionaries=>{
        setList(dictionaries);
    });

    const addDictionary = ()=>{
        if(input === undefined) return;
        let value = input.value
        if(value.trim().length == 0) return;

        fetchCreateDict(value.trim()).then(res=>{
            let ls = list()?.map(id=>id)
            ls?.push(res.created)
            setList(ls)
        })
        
        input.value = ""
    }

    return (
        <Show when={list()} fallback={<LoadingView />}>
            <BorderPage footer={
                <div class="input-bar">
                    <input
                        placeholder="Create dictionary"
                        ref={input}
                    />
                    <img src={add} onclick={addDictionary}/>
                </div>
            } header = {
                <div class="bar">
                    <h1>Dictionaries</h1>
                </div>
            }>
                <ItemList
                    list={list() as Dictionary[]}
                    onClick={prop.onSelect}
                >
                    {item => item.name}
                </ItemList>
            </BorderPage>
        </Show>
    )
}


export function EntryView(prop: {dict: Dictionary, entryName: string, onBack: ()=>void}) {
    let [entry, setEntry] = createSignal<Entry>()
    let [editing, setEditing] = createSignal(false)
    let [editor, setEditor] = createSignal<Quill>()

    fetchEntryData(prop.dict, prop.entryName).then(entry=>{
        setEntry(entry.entry)
    })

    const clickEdit = ()=>{
        if(!editing()){
            setEditing(true)
            return
        }

        let edit = editor()
        if(edit !== undefined){
            setEntry()
            let data = JSON.stringify(edit.getContents().ops)
            fetchEditEntry(prop.dict, prop.entryName, data).then(res=>{
                if(res.success) setEntry({
                    name: prop.entryName,
                    dictid: prop.dict.dictid,
                    data: data
                })
            })
        }
        
        setEditing(false)
    }

    return (
        <BorderPage
            header={
                <div class="bar">
                    <img src={back} onClick={()=>prop.onBack()} />
                    <h1>{prop.dict.name} - {prop.entryName}</h1>
                    <Show when={entry() !== undefined}>
                        <img src={editing() ? check : edit} onClick={clickEdit} />
                    </Show>
                </div>
            }
        >
            <Show when={entry() !== undefined} fallback={<LoadingView />}>
                <div class="view-entry">
                    <div class="section">
                        {entry()?.name}
                    </div>
                    <div class="content">
                        <Show when={editing()} fallback={<TextView data={entry()?.data as string} />}>
                            <TextEdit init={entry()?.data as string} refEditor={setEditor} />
                        </Show>
                    </div>
                </div>
            </Show>
        </BorderPage>
    )
}

export function EntryListView(prop: {
    list?: string[], 
    dictionary: Dictionary,
    onClick: (name: string)=>void, 
    onCreate: (name: string)=>void,
    onBack: ()=>void,
}){
    const [search, setSearch] = createSignal<string>("")
    let input : HTMLInputElement | undefined

    const addEntry = ()=>{
        if(input === undefined) return;
        let val = input.value

        if(val.trim().length === 0) return
        prop.onCreate(val.trim())
    }

    return (
        <BorderPage
            header={
                <div class="bar">
                    <img src={back} onClick={prop.onBack} />
                    <h1>Definitions of {prop.dictionary.name}</h1>
                </div>
            } footer = {
                <div class="input-bar">
                    <input ref={input} onInput={event=>setSearch(event.target.value)} placeholder="Search / Add" />
                    <img src={add} onClick={addEntry}/>
                </div>
            }
        >
            <Show when={prop.list !== undefined} fallback={<LoadingView />}>
                <ItemList
                    list={(prop.list as string[]).filter(v=>v.indexOf(search()) !== undefined)}
                    onClick={item=>prop.onClick(item)}
                >
                    {id=>id}
                </ItemList>
            </Show>
        </BorderPage>
    )
}

export function DictionaryView(prop: {dictionary: Dictionary, onBack: ()=>void}) {
    const [list, setList] = createSignal<string[]>()
    const [entry, setEntry] = createSignal<string>()
    const [loading, setLoading] = createSignal<boolean>(false);
    
    fetchListEntries(prop.dictionary).then(val=>{
        setList(val)
        list()?.sort()
    })

    return (
        <Switch>
            <Match when={loading()}>
                <LoadingView />
            </Match>
            <Match when={entry() === undefined}>
                <EntryListView
                    list={list()}
                    dictionary={prop.dictionary}
                    onClick={entry=>{
                        setEntry(entry)
                    }}
                    onCreate={entry=>{
                        if(entry in (list() as string[])) return;
                        setLoading(true)
                        fetchAddEntry(prop.dictionary, entry).then(value=>{
                            list()?.push(entry)
                            list()?.sort()
                            setEntry(entry)
                            setLoading(false)
                        })
                    }}
                    onBack={prop.onBack}
                />
            </Match>
            <Match when={true}>
                <EntryView dict={prop.dictionary} entryName={entry() as string} onBack={()=>setEntry()} />
            </Match>
        </Switch>
    )
}