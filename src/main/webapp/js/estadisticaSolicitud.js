export class PanelSolicitudes {
  #contenedorPrincipal
  #contextPath
  #getFechaData
  #solicitudes
  #cbxTipoBusqueda
  #txtBuscar
  #btnVerEstadistica
  #popupResultados
  #canva1
  #canva2
  #canva3

  constructor(contenedorPrincipal, getFechaData, contextPath) {
    this.#contenedorPrincipal = contenedorPrincipal
    this.#contextPath = contextPath
    this.#getFechaData = getFechaData
    this.htmlPanel = document.createElement("div")
    this.#solicitudes = []
    this.#configurarHtml()
  }

  async #configurarHtml() {
    this.htmlPanel.className = "container mx-auto space-y-6"
    this.htmlPanel.innerHTML = `
      <h2 class="text-base font-medium text-gray-700 text-2xl mb-2">Criterios de búsqueda específica</h2>
      <div class="flex flex-col md:flex-row items-stretch md:items-center gap-7 mt-2 mb-4">
        <div class="w-full md:w-40">
          <select id="tipoBusqueda"
            class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
            <option value="-1">Todas las solicitudes</option>
            <option value="1">Por colaborador</option>
            <option value="2">Por cliente</option>
          </select>
        </div>
      
        <div class="relative w-full">
          <input type="text" id="txtBuscar" placeholder="Buscar entidad"
            class="w-full md:flex-1 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
          <ul id="popupResultados" tabindex="1"
            class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-md max-h-40 overflow-y-auto hidden">
          </ul>
        </div>
      
        <button type="button"
          class="whitespace-nowrap w-full md:w-auto px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:ring-2 focus:ring-blue-300"
          id="btnVerEstadistica">
          Ver estadísticas
        </button>
      </div>

      <div class="bg-white rounded-xl shadow p-4">
        <canvas id="canva1" class="w-full h-100"></canvas>
      </div>
  
      <div class="flex gap-4">
        
        <div class="w-1/2 bg-white rounded-xl shadow p-4">
          <canvas id="canva2" class="w-full h-74"></canvas>
        </div>
        
        <div class="w-1/2 bg-white rounded-xl shadow p-4">
          <canvas id="canva3" class="w-full h-74"></canvas>
        </div>
  
      </div>
    `
    this.#cbxTipoBusqueda = this.htmlPanel.querySelector("select[id='tipoBusqueda']")
    this.#txtBuscar = this.htmlPanel.querySelector("input[id='txtBuscar']")
    this.#btnVerEstadistica = this.htmlPanel.querySelector("button[id='btnVerEstadistica']")
    this.#popupResultados = this.htmlPanel.querySelector("ul[id='popupResultados']")
    this.#canva1 = this.htmlPanel.querySelector("canvas[id='canva1']")
    this.#canva2 = this.htmlPanel.querySelector("canvas[id='canva2']")
    this.#canva3 = this.htmlPanel.querySelector("canvas[id='canva3']")
    
    this.#configurarBusqueda()
  }

  init() {
    this.#contenedorPrincipal.innerHTML = ""
    this.#contenedorPrincipal.appendChild(this.htmlPanel)
  }

  #configurarBusqueda() {
    let task = null

    this.#popupResultados.addEventListener('click', e => {
      if (e.target.tagName === 'LI') {
        this.#txtBuscar.value = e.target.textContent
        this.#txtBuscar.dataset.id = e.target.dataset.id
        this.#popupResultados.classList.add("hidden")
      }
    });
    this.#popupResultados.addEventListener("focusout", () => {
      setTimeout(() => {
        if (!this.#popupResultados.contains(document.activeElement)) {
          this.#popupResultados.classList.add("hidden")
        }
      }, 0);
    });
    this.#popupResultados.addEventListener("keypress", e => {
      if ((e.key === 'Enter' || e.key === ' ')) {
        e.preventDefault();
        this.#txtBuscar.value = document.activeElement.innerHTML
        this.#txtBuscar.dataset.id = e.target.dataset.id
        this.#popupResultados.classList.add("hidden")
      }
    });

    this.#txtBuscar.addEventListener("focusout", () => {
      setTimeout(() => {
        // if (this.#popupResultados === document.activeElement.parentElement) return
        // this.#popupResultados.classList.add("hidden")
        if (this.#popupResultados !== document.activeElement.parentElement) {
          this.#popupResultados.classList.add("hidden")
        }
      }, 100);
    })
    this.#txtBuscar.addEventListener("focusin", () => {
      if (this.#popupResultados.hasChildNodes()) {
        this.#popupResultados.classList.remove("hidden")
      }
    })

    this.#txtBuscar.addEventListener("input", e => {
      if (this.#cbxTipoBusqueda.value === "-1" || (this.#cbxTipoBusqueda.value !== "1" && this.#cbxTipoBusqueda.value !== "2")) return

      clearTimeout(task)
      task = setTimeout(async () => {
        const tipoBusqueda = this.#cbxTipoBusqueda.value
        const txtTokens = e.target.value.trim().replace(/\s+/g, ' ')
        if (!txtTokens || txtTokens.length === 0) return

        console.log("fecha seleccionada");
        console.log(this.#getFechaData());

        await fetch(`/${this.#contextPath}/control/${tipoBusqueda === "1"? "ColaboradorServlet?estricto=false&": "ClienteServlet?"}action=1&txtTokens=${encodeURIComponent(txtTokens)}`)
          .then(resp => {
            if (resp.ok) {
              return resp.json()
            }
          })
          .then(data => {
            if (data.ok) {
              this.#popupResultados.innerHTML = ""
              let resultados
              if (tipoBusqueda === "1") {
                resultados = data.colaboradores
              } else {
                resultados = data.clientes
              }
              resultados.forEach(resultado => {
                const li = document.createElement("li");
                li.tabIndex = 0
                li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
                li.dataset.id = resultado.id
                li.innerHTML = tipoBusqueda === "1" ? `${resultado.nombre} ${resultado.apellidoPaterno} ${resultado.apellidoMaterno}` :
                resultado.razonSocial
                this.#popupResultados.appendChild(li);
              });
              this.#popupResultados.classList.remove("hidden")
            } else {
              alert(data.error)
            }
          }) 
            // if (colaboradores === null) {
            //   this.#popupResultados.classList.add("hidden")
            //   return
            // }
            // funcPopup(colaboradores) //cambiar
      }, 400);
    })



  }



}
