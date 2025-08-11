import type { Dictionary } from "../types/dictionary";

export async function fetchDictList(){
    return (await fetch("/dict/list")).json() as Promise<Dictionary[]>
}

export async function fetchCreateDict(name: string){
    return (await fetch("/dict/create", {method: "POST", body: JSON.stringify({name: name})})).json() as Promise<{created: Dictionary}>
}

export async function fetchDeleteDict(id: number){
    return (await fetch("/dict/delete", {method: "POST", body: JSON.stringify({id: id})})).json() as Promise<{success: boolean}>
}

