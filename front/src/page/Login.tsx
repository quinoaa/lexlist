import { createSignal, onMount, Show } from "solid-js"
import "./login.scss"

import spinner from "../media/spinner.svg"
import { fetchUserLogin } from "../api/auth";

export function PageLogin(events: {onLogin: ()=>void}) {
    const [awaiting, setAwaiting] = createSignal(false)
    const [error, setError] = createSignal<string|undefined>();

    const [username, setUsername] = createSignal("");
    const [password, setPassword] = createSignal("");

    const handleLogin = ()=>{
        setAwaiting(true);
        console.log(username());
        console.log(password());
        fetchUserLogin(username(), password()).then(res=>{
            if(res.success) events.onLogin();
            else setError("Invalid credentials")
        }).catch(res=>{
            setError("Could not log in")
        }).finally(()=>setAwaiting(false));
    }

    return (<div class="page-login">
        <form class="login-form" onSubmit={()=>handleLogin()}>
            <Show when={!awaiting()} fallback={<img src={spinner} />}>
                <h1>Login</h1>
                <input on:input={event=>setUsername(event.target.value)} placeholder="username"></input>
                <input type="password" on:input={event=>setPassword(event.target.value)} placeholder="password"></input>
                <button onclick={()=>handleLogin()}>Log in</button>
            </Show>
            <Show when={error() !== undefined}>
                <p class="error">{error()}</p>
            </Show>
        </form>
    </div>)
}


