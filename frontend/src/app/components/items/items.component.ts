import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ItemService } from '../../services/item.service';
import { ItemResponseDTO } from './item.model';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {
  items: MatTableDataSource<ItemResponseDTO> = new MatTableDataSource<ItemResponseDTO>();
  totalItems = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private itemService: ItemService) { }

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.itemService.getItems().subscribe(data => {
      this.items = new MatTableDataSource(data);
      this.items.paginator = this.paginator;
      this.totalItems = data.length; // total items count
      this.items.filterPredicate = (data: ItemResponseDTO, filter: string) => {
        return data.itemsCode.toLowerCase().includes(filter) ||
          data.itemsName.toLowerCase().includes(filter) ||
          data.lastReStock.toString().toLowerCase().includes(filter);
      };
    });
  }

  deleteItem(id: number): void {
    this.itemService.deleteItem(id).subscribe(() => {
      this.loadItems();
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.items.filter = filterValue.trim().toLowerCase();

    if (this.items.paginator) {
      this.items.paginator.firstPage();
    }
  }

  pageEvent(event: PageEvent): void {
  }
}
