import { createSignal, Show } from "solid-js";
import type { User } from "../types/user";
import "./app.scss"
import type { Dictionary } from "../types/dictionary";
import { DictionaryList, DictionaryView } from "../components/dictionary";

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