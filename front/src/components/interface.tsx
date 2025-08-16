import "./interface.scss"
import { For, type JSX } from "solid-js"


export type NavBarButton = {
    image: string,
    title: string,
    handle: ()=>void
}

export function NavBar(props: {buttons: NavBarButton[]}){
    return (
        <div class="view-navbar">
            <For each={props.buttons}>
                {btn=>
                    <a>
                        <img src={btn.image}/>
                        <div>{btn.title}</div>
                    </a>
                }
            </For>
        </div>
    )
}

export function BorderPage(prop: {header?: JSX.Element, footer?: JSX.Element, children: JSX.Element}) {
    return (
        <div class="view-borderpage">
            <div class="header">
                {prop.header}
            </div>
            <div class="content">
                {prop.children}
            </div>
            <div class="footer">
                {prop.footer}
            </div>
        </div>
    )
}


export function ItemList<T>(prop: {list: T[], onClick: (item: T, index: number)=>void, children: (item: T)=>string}){
    return (
        <div class="view-itemlist">
            <For each={prop.list}>
                {(item, i)=><div onClick={()=>prop.onClick(item, i())}>{prop.children(item)}</div>}
            </For>
        </div>
    )
}