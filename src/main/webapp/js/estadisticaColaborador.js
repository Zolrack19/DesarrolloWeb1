export class PanelColaboradores {
  #contenedorPrincipal
  #colaboradores
  #canva1
  #canva2
  #canva3

  constructor(contenedorPrincipal) {
    this.htmlPanel = document.createElement("div")
    this.#contenedorPrincipal = contenedorPrincipal
    this.#colaboradores = []
    this.#configurarHtml()
  }
  
  async #configurarHtml() {
    this.htmlPanel.className = "container mx-auto space-y-6"
    this.htmlPanel.innerHTML = `
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
    this.#canva1 = this.htmlPanel.querySelector("canvas[id='canva1']")
    this.#canva2 = this.htmlPanel.querySelector("canvas[id='canva2']")
    this.#canva3 = this.htmlPanel.querySelector("canvas[id='canva3']")
    
    // await fetch()

  }

  init() {
    this.#contenedorPrincipal.innerHTML = ""
    this.#contenedorPrincipal.appendChild(this.htmlPanel)
  }
  
}