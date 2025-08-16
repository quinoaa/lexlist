import { LoadingView } from "../components/loading"
import "./loading.scss"

export function PageLoading(data: {message?: string}){

    return (
        <div class="page-loading">
            <LoadingView message={data.message}/>
        </div>
    )
}

