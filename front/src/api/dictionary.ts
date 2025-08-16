import type { Dictionary, Entry } from "../types/dictionary";

export async function fetchDictList(){
    return (await fetch("/dict/list")).json() as Promise<Dictionary[]>
}

export async function fetchCreateDict(name: string){
    return (await fetch("/dict/create", {method: "POST", body: JSON.stringify({name: name})})).json() as Promise<{created: Dictionary}>
}

export async function fetchDeleteDict(id: number){
    return (await fetch("/dict/delete", {method: "POST", body: JSON.stringify({id: id})})).json() as Promise<{success: boolean}>
}





export async function fetchAddEntry(dict: Dictionary, name: string){
    return (await fetch("/dict/entry/add", {
        method: "POST",
        body: JSON.stringify({
            dictid: dict.dictid,
            name: name
        })
    })).json() as Promise<{created?: Entry}>
}

export async function fetchRemoveEntry(dict: Dictionary, name: string){
    return (await fetch("/dict/entry/remove", {
        method: "POST",
        body: JSON.stringify({
            dictid: dict.dictid,
            name: name
        })
    })).json() as Promise<{success: boolean}>
}

export async function fetchEditEntry(dict: Dictionary, name: string, data: string){
    return (await fetch("/dict/entry/edit", {
        method: "POST",
        body: JSON.stringify({
            dictid: dict.dictid,
            name: name,
            data: data
        })
    })).json() as Promise<{success: boolean}>
}

export async function fetchListEntries(dict: Dictionary){
    return (await fetch("/dict/entry/list", {
        method: "POST",
        body: JSON.stringify({
            dictid: dict.dictid
        })
    })).json() as Promise<string[]>
}

export async function fetchEntryData(dict: Dictionary, name: string){
    return (await fetch("/dict/entry/data", {
        method: "POST",
        body: JSON.stringify({
            dictid: dict.dictid,
            name: name
        })
    })).json() as Promise<{entry?: Entry}>
}


