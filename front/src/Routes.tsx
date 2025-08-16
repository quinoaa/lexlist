import { createSignal, Match, Switch } from 'solid-js';
import { PageLogin } from './page/Login';
import { PageLoading } from './page/Loading';
import type { User } from './types/user';
import { fetchUserState } from './api/auth';
import { App } from './page/App';
import { ViewTextEditor } from './components/text';



type State = {type: "loading", description?: string}|{type: "out"}|{type: "logged", user: User}

function Routes() {

    return <ViewTextEditor />

    let [state, setState] = createSignal<State>({type: "loading"})
    
    let refresh = ()=>{
        if(state().type !== "loading") { setState({type: "loading"}) }

        fetchUserState().then(data=>{
            if(data.logged){
                setState({type: "logged", user: {
                    id: data.id as number,
                    name: data.name as string
                }})
            }else{
                setState({type: "out"})
            }
        }).catch(error=>{
            console.log(error);
            setState({type: "loading", description: "Could not get server respose"})
            setTimeout(refresh, 2000)
        })
    }

    refresh();
    
    return (
        <Switch>
            <Match when={state().type == "loading"}>
                <PageLoading message={(state() as {type: string, description?: string}).description} />
            </Match>
            <Match when={state().type == "out"}>
                <PageLogin onLogin={refresh} />
            </Match>
            <Match when={state().type == "logged"}>
                <App user={(state() as {type: string, user: User}).user} />
            </Match>
        </Switch>
    )
}

export default Routes
