import "./text.scss"
import Quill, { Delta, Op } from "quill";
import "quill/dist/quill.snow.css";
import { QuillDeltaToHtmlConverter } from "quill-delta-to-html";



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
                theme: "snow"
            })
            if(ops !== undefined) quill.setContents(ops);
            
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


