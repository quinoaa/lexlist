import "./text.scss"
import Quill, { Op } from "quill";
import "quill/dist/quill.snow.css";
import { QuillDeltaToHtmlConverter } from "quill-delta-to-html";
import { For, Show } from "solid-js";



export function TextView(props: {data: string}) {

    const init = (el: HTMLDivElement) => {
        if(props.data.length === 0) return;
        try {
            let decoded = JSON.parse(props.data)
            let conv = new QuillDeltaToHtmlConverter(decoded);
            el.innerHTML = conv.convert()
        } catch (error) {
            el.innerText = `Could not load content: ${error}`
        }
    }

    return (
        <div ref={init} />
    )
}

export function TextEdit(props: {init: string, refEditor: (quill: Quill)=>void}) {
    
    let setup = (editor: HTMLDivElement)=>{

        try{
            let ops: Op[]|undefined
            if(props.init.length !== 0) ops = JSON.parse(props.init)
            
            let quill = new Quill(editor, {
                theme: "snow",
                modules: {
                    toolbar: [
                        ["bold", "italic"],
                        [{header: 1}]
                    ]
                }
            })
            if(ops !== undefined) quill.setContents(ops);
            console.log(ops);
            
            
            props.refEditor(quill)
        }catch (error) {
            console.error(error);
        }
    }


    return (
        <div>
            <div ref={setup} />
        </div>
    )
}


export type TextPart = [format: string, text: string]

export type TextLine = {
    text: TextPart[]
}



function TextLineRender(props: {text: TextLine}) {
    return (
        <For each={props.text.text}>{
            part=>{
                const [format, text] = part;
                let classes = ""
                for(let i = 0;i < format.length;i ++) classes += format[i] + " "

                return (
                    <span class={classes}>{text}</span>
                )
            }
        }</For>
    )
}

function TextRender(props: {lines: TextLine[]}){
    return (
        <For each={props.lines}>{
            line => <TextLineRender text={line} />
        }</For>
    )
}

export function ViewText(props: {text: TextLine[]}) {
    return (
        <div class="view-text">
            <TextRender lines={props.text} />
        </div>
    )
}

function isParent(child: Node, parent: Node){
    let current = child.parentNode
    if(current == undefined) return false;
    if(current === parent) return true;

    return isParent(current, parent);
}


function applyRange(
    node: Range,
    apply: (node: Node, start?: number, end?: number)=>void
) {
    if(node.startContainer === node.endContainer){
        apply(node.startContainer, node.startOffset, node.endOffset)
        return
    }

    
    
}

function toggleFormatting(range: Range, classname: string) {
    let selStart: Node|undefined, selEnd: Node|undefined

    applyRange(range, (node, start, end)=>{
        let parent = node.parentNode
        if(parent == undefined) return;

        let textBegin : string|undefined, textEnd : string|undefined
        let text = node.textContent
        if(text == undefined) return;

        if(end){
            textEnd = text.substring(end)
            text = text.substring(0, end)
        }
        if(start){
            textBegin = text.substring(0, start)
            text = text.substring(start)
        }

        if(textBegin && textBegin.length != 0) {
            parent.insertBefore(document.createTextNode(textBegin), node)
        }
        const el = document.createElement("span")
        el.className = classname
        el.innerText = text
        parent.insertBefore(el, node)
        if(textEnd && textEnd.length != 0) {
            parent.insertBefore(document.createTextNode(textEnd), node)
        }
        if(range.startContainer === node) selStart = el
        if(range.endContainer === node) selEnd = el
        parent.removeChild(node)
    })

    if(selStart && selEnd){
        range.setStartBefore(selStart)
        range.setEndAfter(selEnd)
    }
}

export function ViewTextEditor(props: {text?: TextLine[]}) {
    let edit: HTMLDivElement|undefined

    const toggleFormat = (format: string, event: MouseEvent)=>{
        event.preventDefault()
        const range = getSelection()?.getRangeAt(0);
        if(range === undefined || edit === undefined) return;
        if(!isParent(range.commonAncestorContainer, edit)) return;
        
        toggleFormatting(range, format)
        
    }

    return (
        <div class="view-texteditor">
            <div class="toolbar" >
                <a onMouseDown={(e)=>toggleFormat("b", e)}>B</a>
                <a onMouseDown={(e)=>toggleFormat("i", e)}>I</a>
                <a onMouseDown={(e)=>toggleFormat("u", e)}>U</a>
            </div>
            <div class="view-text" contentEditable={true} ref={edit}>
                <Show when={props.text}>
                    <TextRender lines={props.text as TextLine[]}/>
                </Show>
            </div>
        </div>
    )
}

