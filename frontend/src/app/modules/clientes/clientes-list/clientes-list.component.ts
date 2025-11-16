import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';

import { ClienteService } from '@core/services/cliente.service';
import { Cliente } from '@shared/models/cliente.model';

@Component({
  selector: 'app-clientes-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './clientes-list.component.html',
  styleUrls: ['./clientes-list.component.scss']
})
export class ClientesListComponent implements OnInit {
  private clienteService = inject(ClienteService);

  clientes = signal<Cliente[]>([]);
  loading = signal(false);
  searchTerm = '';
  displayedColumns: string[] = ['nombreCompleto', 'ci', 'nit', 'celular', 'acciones'];

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(): void {
    this.loading.set(true);
    this.clienteService.findAll(0, 20).subscribe({
      next: (response) => {
        this.loading.set(false);
        if (response.success && response.data) {
          this.clientes.set(response.data.content);
        }
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  search(): void {
    if (this.searchTerm.trim()) {
      this.loading.set(true);
      this.clienteService.search(this.searchTerm, 0, 20).subscribe({
        next: (response) => {
          this.loading.set(false);
          if (response.success && response.data) {
            this.clientes.set(response.data.content);
          }
        },
        error: () => {
          this.loading.set(false);
        }
      });
    } else {
      this.loadClientes();
    }
  }

  edit(cliente: Cliente): void {
    console.log('Editar cliente:', cliente);
    // Implementar navegación a formulario de edición
  }

  delete(cliente: Cliente): void {
    console.log('Eliminar cliente:', cliente);
    // Implementar confirmación y eliminación
  }
}
