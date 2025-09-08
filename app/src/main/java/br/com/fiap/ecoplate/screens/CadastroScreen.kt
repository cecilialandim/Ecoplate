package br.com.fiap.cadastro.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

val Green = Color(red = 46, green = 125, blue = 50)


data class Produto(
    val nome: String,
    val descricao: String,
    val dataValidade: String,
    val quantidade: String,
    val categoria: String,
    val valor: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroProdutoScreen() {
    val produtos = remember { mutableStateListOf<Produto>() }

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var dataValidade by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }

    val categorias = listOf(
        "Frios e Laticínios",
        "Bebidas",
        "Feira",
        "Congelados",
        "Molhos e condimentos",
        "Não perecíveis"
    )
    var expanded by remember { mutableStateOf(false) }
    var produtoEditando by remember { mutableStateOf<Produto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (produtoEditando == null) "Cadastro" else "Editar Produto",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Green,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Nome do Item", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    placeholder = { Text("Ex: Leite Integral") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Descrição", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    placeholder = { Text("Ex: Caixa 1L, marca X") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Data de Validade", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                OutlinedTextField(
                    value = dataValidade,
                    onValueChange = { dataValidade = it },
                    placeholder = { Text("10/05/2024") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Quantidade", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    placeholder = { Text("0") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Valor", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    placeholder = { Text("R$0,00") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text("Categoria", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecione") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categorias.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    categoria = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (nome.isNotBlank() && categoria.isNotBlank()) {
                            if (produtoEditando == null) {
                                produtos.add(
                                    Produto(nome, descricao, dataValidade, quantidade, categoria, valor)
                                )
                            } else {
                                val index = produtos.indexOf(produtoEditando!!)
                                if (index != -1) {
                                    produtos[index] =
                                        Produto(nome, descricao, dataValidade, quantidade, categoria, valor)
                                }
                                produtoEditando = null
                            }
                            nome = ""
                            descricao = ""
                            dataValidade = ""
                            quantidade = ""
                            categoria = ""
                            valor = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) {
                    Text(
                        text = if (produtoEditando == null) "Salvar Produto" else "Atualizar Produto",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(produtos) { produto ->
                ProdutoCard(
                    produto = produto,
                    onEditar = {
                        produtoEditando = produto
                        nome = produto.nome
                        descricao = produto.descricao
                        dataValidade = produto.dataValidade
                        quantidade = produto.quantidade
                        categoria = produto.categoria
                        valor = produto.valor
                    },
                    onExcluir = { produtos.remove(produto) }
                )
            }
        }
    }
}

@Composable
fun ProdutoCard(
    produto: Produto,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(produto.nome, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(produto.descricao, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Validade: ${produto.dataValidade}", fontSize = 14.sp)
            Text("Quantidade: ${produto.quantidade}", fontSize = 14.sp)
            Text("Categoria: ${produto.categoria}", fontSize = 14.sp)
            Text("Valor: ${produto.valor}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botão Editar (estilo do Salvar Produto)
                Button(
                    onClick = onEditar,
                    modifier = Modifier.height(45.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) {
                    Text(
                        text = "Editar",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onExcluir,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CadastroPreview() {
    CadastroProdutoScreen()
}
