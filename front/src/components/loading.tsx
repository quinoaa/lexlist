import { Show } from "solid-js";
import spinner from "../media/spinner.svg"
import "./loading.scss"

export function LoadingView(data: {message?: string}) {

    return (
        <div class="view-loading">
            <img src={spinner} />
            <Show when={data.message !== undefined}>
                <p>{data.message}</p>
            </Show>
        </div>
    )
}