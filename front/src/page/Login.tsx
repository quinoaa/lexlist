import { createSignal, Show } from "solid-js"
import "./login.scss"

import spinner from "../media/spinner.svg"

export function PageLogin(onLogin: {onLogin: ()=>void}) {
    const [awaiting, setAwaiting] = createSignal(false)

    const handleLogin = ()=>{
        setAwaiting(true)
    }

    return (<>
        <div id="login-form">
            <Show when={!awaiting()}>
                <h1>Login</h1>
                {awaiting()}
                <input id="username"></input>
                <input id="password"></input>
                <button onclick={()=>handleLogin()}>Log in</button>
                <svg>{spinner}</svg>
            </Show>

        </div>
    </>)
}


